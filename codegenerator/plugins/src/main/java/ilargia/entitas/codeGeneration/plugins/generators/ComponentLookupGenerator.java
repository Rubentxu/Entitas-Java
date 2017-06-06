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
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

import java.util.*;
import java.util.stream.Collectors;

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.getTypeName;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsDataProvider.getContextNames;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods;
import static ilargia.entitas.codeGeneration.plugins.generators.ComponentEntityGenerator.DEFAULT_COMPONENT_LOOKUP_TAG;


public class ComponentLookupGenerator implements ICodeGenerator<JavaClassSource>, IConfigurable {
    private TargetPackageConfig targetPackageConfig;
    private Map<String, CodeGenFile<JavaClassSource>> lookups;
    private Map<String, List<ComponentData>> contextDatas;

    public ComponentLookupGenerator() {
        targetPackageConfig = new TargetPackageConfig();
        lookups = new HashMap<>();
        contextDatas = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Component (Matcher API)";
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
    public Properties getDefaultProperties() {
        return targetPackageConfig.getDefaultProperties();
    }

    @Override
    public void configure(Properties properties) {
        targetPackageConfig.configure(properties);
    }

    @Override
    public List<CodeGenFile<JavaClassSource>> generate(List<CodeGeneratorData> data) {
        List<ComponentData> finalDatas = data.stream()
                .filter(d -> d instanceof ComponentData)
                .map(d -> (ComponentData) d)
                .filter(d -> shouldGenerateMethods(d))
                .sorted((a,b)-> a.getSource().getName().compareTo(b.getSource().getName()))
                .collect(Collectors.toList());

        finalDatas.stream().forEach(d -> storeContextData(d));

        contextDatas.forEach((k, v)-> generateIndicesLookup(k, v));

        lookups.values().forEach(f -> System.out.println(f.getFileContent().toString()));
        return lookups.values().stream().collect(Collectors.toList());
    }

    private void storeContextData(ComponentData data) {
        getContextNames(data).stream().forEach(contextName -> {
            if (!contextDatas.containsKey(contextName)) contextDatas.put(contextName, new ArrayList<>());
            contextDatas.get(contextName).add(data);
        });
    }


    private JavaClassSource generateIndicesLookup(String contextName, List<ComponentData> data) {
        String pkgDestiny = targetPackageConfig.targetPackage();

        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG));

        if (data.size() > 0 && !pkgDestiny.endsWith(data.get(0).getSubDir())) {
            pkgDestiny += "." + data.get(0).getSubDir();

        }
        javaClass.setPackage(pkgDestiny);

        addIndices(data, javaClass);
        addComponentNames(data, javaClass);
        addComponentTypes(data, javaClass);
        System.out.println(javaClass);
        return javaClass;
    }


    public JavaClassSource addIndices(List<ComponentData> datas, JavaClassSource javaClass) {
        Integer index = 0;
        for (ComponentData data : datas) {
            if (data != null) {
                javaClass.addField()
                        .setName(getTypeName(data))
                        .setType("int")
                        .setLiteralInitializer(String.valueOf(index++))
                        .setPublic()
                        .setStatic(true)
                        .setFinal(true);
            }
        }
        javaClass.addField()
                .setName("totalComponents")
                .setType("int")
                .setLiteralInitializer(Integer.toString(datas.size()))
                .setPublic()
                .setStatic(true)
                .setFinal(true);

        return javaClass;

    }

    public void addComponentNames(List<ComponentData> dataList, JavaClassSource javaClass) {
        String format = " \"%1$s\",\n";
        String code = " return new String[] {";

        ArrayList<ComponentData> totalData = new ArrayList<>(Collections.nCopies(dataList.size(), null));
        for (ComponentData info : dataList) {
            totalData.add(info.index, info);
        }
        totalData.subList(dataList.get(0).totalComponents, totalData.size()).clear();
        for (int i = 0; i < totalData.size(); i++) {
            ComponentData info = totalData.get(i);
            if (info != null && info.index == i) {
                code += String.format(format, getTypeName(dataList));
            } else {
                code += " null,\n";
            }

        }


        StringBuilder codeFinal = new StringBuilder(code);
        if (code.endsWith(",\n")) {
            codeFinal.replace(code.lastIndexOf(",\n"), code.lastIndexOf(",") + 1, " }; ");
        }
        javaClass.addMethod().setName("componentNames")
                .setReturnType("String[]")
                .setPublic()
                .setStatic(true)
                .setBody(codeFinal.toString());

    }

    public void addComponentTypes(List<ComponentData> data, JavaClassSource javaClass) {
        String format = " %1$s%2$s,\n";
        String code = "return new Class[] {";
        ArrayList<ComponentData> totalInfos = new ArrayList<>(Collections.nCopies(data.get(0).totalComponents, null));
        for (ComponentData info : data) {
            totalInfos.add(info.index, info);
        }
        totalInfos.subList(data.get(0).totalComponents, totalInfos.size()).clear();
        for (int i = 0; i < totalInfos.size(); i++) {
            ComponentData info = totalInfos.get(i);
            if (info != null && info.index == i) {
                code += String.format(format, getTypeName(data), ".class");
                javaClass.addImport(info.fullTypeName);
            } else {
                code += String.format(format, null, "");
            }

        }
        StringBuilder codeFinal = new StringBuilder(code);
        if (code.endsWith(",\n")) {
            codeFinal.replace(code.lastIndexOf(",\n"), code.lastIndexOf(",") + 1, " }; ");
        }

        javaClass.addMethod()
                .setName("componentTypes")
                .setReturnType("Class[]")
                .setPublic()
                .setStatic(true)
                .setBody(codeFinal.toString());

    }

    public void addComponentFactories(ComponentData[] data, JavaClassSource javaClass) {
        String format = " %1$s.class,\n";
        String code = "return new FactoryComponent[] {";
        for (int i = 0; i < data.length; i++) {
            ComponentData info = data[i];
            JavaInterfaceSource interfaceSource = Roaster.parse(JavaInterfaceSource.class, String.format("public interface Factory%1$s extends FactoryComponent {}",
                    getTypeName(data)));
            interfaceSource.addMethod()
                    .setName(String.format("create%1$s", getTypeName(data)))
                    .setReturnType(getTypeName(data))
                    .setPublic();

            javaClass.addNestedType(interfaceSource.toString());


        }


    }
}
