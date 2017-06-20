package ilargia.entitas.codeGeneration.plugins.generators;

import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.config.TargetPackageConfig;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.apache.commons.lang3.text.WordUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.TypeVariableSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.*;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ConstructorDataProvider.getConstructorData;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsComponentDataProvider.getContextNames;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsComponentDataProvider.isSharedContext;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.EnumsDataProvider.getEnumData;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.GenericsDataProvider.getGenericsData;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.IsUniqueDataProvider.isUnique;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.MemberDataProvider.getMemberData;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.MemberDataProvider.isFlagComponent;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods;
import static ilargia.entitas.codeGeneration.plugins.generators.EntitasGenerator.DEFAULT_COMPONENT_LOOKUP_TAG;


public class ComponentEntityGenerator implements ICodeGenerator<JavaClassSource>, IConfigurable {
    private TargetPackageConfig targetPackageConfig;
    private Map<String, CodeGenFile<JavaClassSource>> entities;

    public ComponentEntityGenerator() {
        targetPackageConfig = new TargetPackageConfig();
        entities = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Component (Entity API)";
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
                .filter(d -> !d.containsKey("entityIndex_type"))
                .map(d -> (ComponentData) d)
                .filter(d -> shouldGenerateMethods(d))
                .forEach(d -> generateEntities(d));

        entities.values().forEach(f -> System.out.println(f.getFileContent().toString()));
        return entities.values().stream().collect(Collectors.toList());
    }

    private void generateEntities(ComponentData data) {
        getContextNames(data).stream()
                .forEach(contextName -> generateEntity(contextName, data));
    }

    private void generateEntity(String contextName, ComponentData data) {
        String pkgDestiny = targetPackageConfig.getTargetPackage();
        CodeGenFile<JavaClassSource> genFile = getCodeGenFile(contextName, data);
        JavaClassSource codeGenerated = genFile.getFileContent();

        if (!pkgDestiny.endsWith(data.getSubDir())) {
            pkgDestiny += "." + data.getSubDir();

        }
        if (codeGenerated.getPackage() == null) {
            codeGenerated.setPackage(pkgDestiny);
            codeGenerated.addMethod()
                    .setName(contextName + "Entity")
                    .setPublic()
                    .setConstructor(true)
                    .setBody("");
            codeGenerated.addImport("ilargia.entitas.Entity");

        }

        if (shouldGenerateMethods(data)) {
            addImporEnums(data, codeGenerated);
            addEntityMethods(contextName, data, codeGenerated);
        }
        if (isSharedContext(data) && codeGenerated.getImport(targetPackageConfig.getTargetPackage()) == null) {
            codeGenerated.addImport(targetPackageConfig.getTargetPackage() + ".SharedComponentsLookup");
        }

    }

    private CodeGenFile<JavaClassSource> getCodeGenFile(String contextName, ComponentData data) {

        if (entities.containsKey(contextName)) {
            return entities.get(contextName);
        } else {
            JavaClassSource sourceGen = Roaster.parse(JavaClassSource.class, String.format("public class %1$sEntity extends Entity {}", contextName));
            CodeGenFile<JavaClassSource> genFile = new CodeGenFile<JavaClassSource>(contextName + "Entity", sourceGen, data.getSubDir());
            entities.put(contextName, genFile);
            return genFile;
        }
    }

    private void addImporEnums(ComponentData data, JavaClassSource codeGenerated) {
        List<String> enums = getEnumData(data);
        if (enums != null)
            enums.forEach(e -> codeGenerated.addImport(e));

    }

    private void addEntityMethods(String contextName, ComponentData data, JavaClassSource codedGenerated) {
        addGetMethods(contextName, data, codedGenerated);
        addHasMethods(contextName, data, codedGenerated);
        addAddMethods(contextName, data, codedGenerated);
        addReplaceMethods(contextName, data, codedGenerated);
        addRemoveMethods(contextName, data, codedGenerated);
        addImportClass(data, codedGenerated);

    }

    private void addGetMethods(String contextName, ComponentData data, JavaClassSource codeGenerated) {
        codeGenerated.addImport(getFullTypeName(data));

        if (isUnique(data)) {
            codeGenerated.addField()
                    .setName(getTypeNameSuffix(data))
                    .setType(getTypeName(data))
                    .setLiteralInitializer(String.format("new %1$s();", getTypeName(data)))
                    .setPublic();

        } else {
            MethodSource<JavaClassSource> methodGET = codeGenerated.addMethod()
                    .setName(String.format("get%1$s", getTypeName(data)))
                    .setReturnType(getTypeName(data))
                    .setPublic();

            String typeName = getTypeName(data);
            List<TypeVariableSource<JavaClassSource>> generics = getGenericsData(data);
            if (generics != null && generics.size() > 0) {
                typeName += "<";
                for (TypeVariableSource<JavaClassSource> generic : generics) {
                    String javaType[] = new String[generic.getBounds().size()];
                    for (int i = 0; i < generic.getBounds().size(); i++) {
                        javaType[i] = (String) generic.getBounds().get(i).getSimpleName();
                    }
                    methodGET.addTypeVariable().setName(generic.getName()).setBounds(javaType);
                    if (typeName.indexOf("<") != typeName.length() - 1) typeName += ",";
                    typeName += generic.getName();
                }
                typeName += ">";

            }
            methodGET.setBody(String.format("return (%1$s)getComponent(%2$s.%3$s);"
                    , typeName, (isSharedContext(data) ? "Shared" : contextName) + DEFAULT_COMPONENT_LOOKUP_TAG, getTypeName(data)));
        }
    }

    private void addHasMethods(String contextName, ComponentData data, JavaClassSource source) {
        source.addImport(getFullTypeName(data));

        if (isUnique(data)) {
            source.addMethod()
                    .setName(String.format("is%1$s", getTypeName(data)))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            WordUtils.capitalize(isSharedContext(data) ? "Shared" : contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                            getTypeName(data)));

            source.addMethod()
                    .setName(String.format("set%1$s", getTypeName(data)))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters("boolean value")
                    .setBody(String.format(" if(value != hasComponent(%1$s.%2$s)) {\n" +
                                    "                    if(value) {\n" +
                                    "                        addComponent(%1$s.%2$s, %3$s);\n" +
                                    "                    } else {\n" +
                                    "                        removeComponent(%1$s.%2$s);\n" +
                                    "                    }\n" +
                                    "                }\n return this;", WordUtils.capitalize((isSharedContext(data) ? "Shared" : contextName) + DEFAULT_COMPONENT_LOOKUP_TAG),
                            getTypeName(data), getTypeNameSuffix(data)));


        } else {
            source.addMethod()
                    .setName(String.format("has%1$s", getTypeName(data)))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            WordUtils.capitalize((isSharedContext(data) ? "Shared" : contextName) + DEFAULT_COMPONENT_LOOKUP_TAG),
                            getTypeName(data)));

        }
    }


    public void addImportClass(ComponentData data, JavaClassSource codeGenerated) {
        if (data.getSource().getImports() != null) {
            data.getSource().getImports().stream()
                    .filter(i -> !i.getQualifiedName().contains("ilargia.entitas.codeGenerator.annotations"))
                    .filter(i -> !i.getQualifiedName().equals("ilargia.entitas.api.IComponent"))
                    .forEach(imp -> codeGenerated.addImport(imp));
        }
    }

    private void addAddMethods(String contextName, ComponentData data, JavaClassSource codeGenerated) {
        if (!isFlagComponent(data)) {
            MethodSource<JavaClassSource> addMethod = codeGenerated.addMethod()
                    .setName(String.format("add%1$s", getTypeName(data)))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(getConstructorData(data) != null && getConstructorData(data).size() > 0
                            ? memberNamesWithTypeFromConstructor(getConstructorData(data).get(0))
                            : memberNamesWithType(getMemberData(data)));

            String typeName = getTypeName(data);
            List<TypeVariableSource<JavaClassSource>> generics = getGenericsData(data);
            if (generics != null && generics.size() > 0) {
                typeName += "<";
                for (TypeVariableSource<JavaClassSource> generic : generics) {
                    String javaType[] = new String[generic.getBounds().size()];
                    for (int i = 0; i < generic.getBounds().size(); i++) {
                        javaType[i] = (String) generic.getBounds().get(i).getSimpleName();
                    }
                    addMethod.addTypeVariable().setName(generic.getName()).setBounds(javaType);
                    if (typeName.indexOf("<") != typeName.length() - 1) typeName += ",";
                    typeName += generic.getName();
                }
                typeName += ">";
            }
            String method = "";

            List<MethodSource<JavaClassSource>> constructors = getConstructorData(data);
            if (constructors != null && constructors.size() > 0) {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%5$s);\n if(component == null) { " +
                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} addComponent(%1$s.%5$s, component);\n return this;",
                        WordUtils.capitalize(isSharedContext(data) ? "Shared" : contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, bodyFromConstructor(constructors.get(0)), memberNamesFromConstructor(constructors.get(0))
                        , getTypeName(data));

            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n } \n%3$s\n addComponent(%1$s.%2$s, component);\n return this;",
                        WordUtils.capitalize(isSharedContext(data) ? "Shared" : contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, memberAssignments(getMemberData(data)), getTypeName(data));
            }
            addMethod.setBody(method);

        }
    }


    private void addReplaceMethods(String contextName, ComponentData data, JavaClassSource codeGenerated) {
        if (!isFlagComponent(data)) {
            List<MethodSource<JavaClassSource>> constructors = getConstructorData(data);
            MethodSource<JavaClassSource> replaceMethod = codeGenerated.addMethod()
                    .setName(String.format("replace%1$s", getTypeName(data)))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(constructors != null && constructors.size() > 0
                            ? memberNamesWithTypeFromConstructor(constructors.get(0))
                            : memberNamesWithType(getMemberData(data)));

            String typeName = getTypeName(data);
            List<TypeVariableSource<JavaClassSource>> generics = getGenericsData(data);
            if (generics != null && generics.size() > 0) {
                typeName += "<";
                for (TypeVariableSource<JavaClassSource> generic : generics) {
                    String javaType[] = new String[generic.getBounds().size()];
                    for (int i = 0; i < generic.getBounds().size(); i++) {
                        javaType[i] = (String) generic.getBounds().get(i).getSimpleName();
                    }
                    replaceMethod.addTypeVariable().setName(generic.getName()).setBounds(javaType);
                    if (typeName.indexOf("<") != typeName.length() - 1) typeName += ",";
                    typeName += generic.getName();
                }
                typeName += ">";
            }
            String method;
            if (constructors != null && constructors.size() > 0) {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%5$s);\n if(component == null) { " +
                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} replaceComponent(%1$s.%5$s, component);\n return this;"
                        , WordUtils.capitalize(isSharedContext(data) ? "Shared" : contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, bodyFromConstructor(constructors.get(0)), memberNamesFromConstructor(constructors.get(0))
                        , getTypeName(data));
            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n} %3$s\n replaceComponent(%1$s.%2$s, component);\n return this;",
                        WordUtils.capitalize(isSharedContext(data) ? "Shared" : contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, memberAssignments(getMemberData(data)), getTypeName(data));
            }
            replaceMethod.setBody(method);

        }
    }

    private void addRemoveMethods(String contextName, ComponentData data, JavaClassSource codedGenerated) {
        if (!isFlagComponent(data)) {
            String method = "removeComponent(%1$s.%2$s);return this;";
            codedGenerated.addMethod()
                    .setName(String.format("remove%1$s", getTypeName(data)))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setBody(String.format(method,
                            WordUtils.capitalize(isSharedContext(data) ? "Shared" : contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                            getTypeName(data)));


        }
    }

    public String memberNamesWithType(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(info -> info.getType() + " " + info.getName())
                .collect(Collectors.joining(", "));

    }

    public String memberNames(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(info -> info.getName())
                .collect(Collectors.joining(", "));
    }

    public String memberNamesFromConstructor(MethodSource<JavaClassSource> constructor) {
        return constructor.getParameters().stream()
                .map(info -> info.getName())
                .collect(Collectors.joining(", "));
    }

    public String memberNamesWithTypeFromConstructor(MethodSource<JavaClassSource> constructor) {
        return constructor.getParameters().stream()
                .map(info -> info.toString())
                .collect(Collectors.joining(", "));
    }

    public String bodyFromConstructor(MethodSource<JavaClassSource> constructor) {
        return constructor.getBody().replaceAll("this", "component");
    }

    public String memberAssignments(List<FieldSource<JavaClassSource>> memberInfos) {
        String format = "component.%1$s = %2$s;";
        return memberInfos.stream().map(
                info -> {
                    String newArg = info.getName();
                    return String.format(format, info.getName(), newArg);
                }
        ).collect(Collectors.joining("\n"));

    }


}
