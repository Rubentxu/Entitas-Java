package ilargia.entitas.codeGeneration.plugins.generators;

import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.config.TargetPackageConfig;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.apache.commons.lang3.text.WordUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.*;
import java.util.stream.Collectors;

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.getFullTypeName;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.getTypeName;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsComponentDataProvider.getContextNames;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsComponentDataProvider.isSharedContext;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods;


public class ComponentLookupGenerator implements ICodeGenerator<JavaClassSource>, IConfigurable {
    private static final String DEFAULT_COMPONENT_LOOKUP_TAG = "ComponentsLookup";
    private TargetPackageConfig targetPackageConfig;

    private Map<String, List<ComponentData>> contextDatas;

    public ComponentLookupGenerator() {
        targetPackageConfig = new TargetPackageConfig();
        contextDatas = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Components Lookup";
    }

    @Override
    public Integer gePriority() {
        return 0;
    }

    @Override
    public boolean isEnableByDefault() {
        return true;
    }

    @Override
    public boolean runInDryMode() {
        return true;
    }

    @Override
    public Properties defaultProperties() {
        return targetPackageConfig.defaultProperties();
    }

    @Override
    public void setProperties(Properties properties) {
        targetPackageConfig.setProperties(properties);
    }

    @Override
    public List<CodeGenFile<JavaClassSource>> generate(List<CodeGeneratorData> data) {
        data.stream()
                .filter(d -> d instanceof ComponentData)
                .map(d -> (ComponentData) d)
                .filter(d-> !d.containsKey("entityIndex_type"))
                .filter(d -> shouldGenerateMethods(d))
                .sorted((a, b) -> a.getSource().getName().compareTo(b.getSource().getName()))
                .forEach(d -> storeContextData(d));

        List<CodeGenFile<JavaClassSource>> result =  contextDatas.entrySet().stream()
                .map((entry) -> {
                    JavaClassSource codeGen = generateIndicesLookup(entry.getKey(), entry.getValue());
                    return new CodeGenFile<JavaClassSource>(codeGen.getName(), codeGen, entry.getValue().get(0).getSubDir());
                })
                .collect(Collectors.toList());

        contextDatas.clear();
        return result;

    }

    private void storeContextData(ComponentData data) {
        if (getContextNames(data).size() > 1) {
            if (!contextDatas.containsKey("shared")) contextDatas.put("shared", new ArrayList<>());
            contextDatas.get("shared").add(data);
        } else {
            getContextNames(data).stream().forEach(contextName -> {
                if (!contextDatas.containsKey(contextName)) contextDatas.put(contextName, new ArrayList<>());
                contextDatas.get(contextName).add(data);
            });
        }
    }

    private JavaClassSource generateIndicesLookup(String contextName, List<ComponentData> dataList) {
        String pkgDestiny = targetPackageConfig.getTargetPackage();

        JavaClassSource codeGen = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG));
        if (dataList.size() > 0 && !pkgDestiny.endsWith(dataList.get(0).getSubDir()) ) {
           // pkgDestiny += "." + dataList.get(0).getSubDir();

        }
        codeGen.setPackage(pkgDestiny);

        addIndices(dataList, codeGen);
        addComponentNames(dataList, codeGen);
        addComponentTypes(dataList, codeGen);
        System.out.println(codeGen);

        return codeGen;
    }


    public JavaClassSource addIndices(List<ComponentData> dataList, JavaClassSource codeGenerated) {
        final Integer[] index = {0};
        dataList.stream()
                //.sorted((a, b) -> a.getSource().getName().compareTo(b.getSource().getName()))
                .map(d -> {
                    d.put("index", index[0]++);
                    return d;
                })
                .forEach(data -> {
                    if (data != null) {
                        codeGenerated.addField()
                                .setName(getTypeName(data))
                                .setType("int")
                                .setLiteralInitializer(data.get("index").toString())
                                .setPublic()
                                .setStatic(true)
                                .setFinal(true);
                    }
                });
        codeGenerated.addField()
                .setName("totalComponents")
                .setType("int")
                .setLiteralInitializer(Integer.toString(dataList.size()))
                .setPublic()
                .setStatic(true)
                .setFinal(true);

        return codeGenerated;

    }

    public void addComponentNames(List<ComponentData> dataList, JavaClassSource codeGenerated) {
        String format = " \"%1$s\",\n";
        String code = " return new String[] {";

        for (ComponentData data : dataList) {
            code += String.format(format, getTypeName(data));

        }

        StringBuilder codeFinal = new StringBuilder(code);
        if (code.endsWith(",\n")) {
            codeFinal.replace(code.lastIndexOf(",\n"), code.lastIndexOf(",") + 1, " }; ");
        }
        codeGenerated.addMethod().setName("componentNames")
                .setReturnType("String[]")
                .setPublic()
                .setStatic(true)
                .setBody(codeFinal.toString());

    }

    public void addComponentTypes(List<ComponentData> dataList, JavaClassSource codGenerated) {
        String format = " %1$s%2$s,\n";
        String code = "return new Class[] {";

        for (ComponentData data : dataList) {
            code += String.format(format, getTypeName(data), ".class");
            codGenerated.addImport(getFullTypeName(data));

        }

        StringBuilder codeFinal = new StringBuilder(code);
        if (code.endsWith(",\n")) {
            codeFinal.replace(code.lastIndexOf(",\n"), code.lastIndexOf(",") + 1, " }; ");
        }

        codGenerated.addMethod()
                .setName("componentTypes")
                .setReturnType("Class[]")
                .setPublic()
                .setStatic(true)
                .setBody(codeFinal.toString());

    }

//    public void addComponentFactories(ComponentData[] data, JavaClassSource codeGenerated) {
//        String format = " %1$s.class,\n";
//        String code = "return new FactoryComponent[] {";
//        for (int i = 0; i < data.length; i++) {
//            ComponentData info = data[i];
//            JavaInterfaceSource interfaceSource = Roaster.parse(JavaInterfaceSource.class, String.format("public interface Factory%1$s extends FactoryComponent {}",
//                    getTypeName(data)));
//            interfaceSource.addMethod()
//                    .setName(String.format("create%1$s", getTypeName(data)))
//                    .setReturnType(getTypeName(data))
//                    .setPublic();
//
//            codeGenerated.addNestedType(interfaceSource.toString());
//
//
//        }
//
//
//    }
}
