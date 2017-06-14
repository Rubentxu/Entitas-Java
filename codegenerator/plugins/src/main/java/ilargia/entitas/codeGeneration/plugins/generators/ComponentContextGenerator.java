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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.getFullTypeName;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.getTypeName;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ConstructorDataProvider.getConstructorData;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsComponentDataProvider.getContextNames;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.IsUniqueDataProvider.isUnique;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.MemberDataProvider.getMemberData;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods;


public class ComponentContextGenerator implements ICodeGenerator<JavaClassSource>, IConfigurable {
    private TargetPackageConfig targetPackageConfig;
    private Map<String, CodeGenFile<JavaClassSource>> contexts;

    public ComponentContextGenerator() {
        targetPackageConfig = new TargetPackageConfig();
        contexts = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Component (Context API)";
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
        data.stream()
                .filter(d -> d instanceof ComponentData)
                .map(d -> (ComponentData) d)
                .filter(d -> shouldGenerateMethods(d))
                .forEach(d -> generateContexts(d));

        contexts.values().forEach(f -> System.out.println(f.getFileContent().toString()));
        return contexts.values().stream().collect(Collectors.toList());
    }

    private void generateContexts(ComponentData data) {
        getContextNames(data).stream()
                .forEach(contextName -> generateContext(contextName, data));
    }

    private void generateContext(String contextName, ComponentData data) {
        String pkgDestiny = targetPackageConfig.getTargetPackage();

        if (!contexts.containsKey(contextName)) {
            JavaClassSource sourceGen = Roaster.parse(JavaClassSource.class, String.format("public class %1$sContext extends Context<%1$sEntity> {}", contextName));
            CodeGenFile<JavaClassSource> genFile = new CodeGenFile<JavaClassSource>(contextName + "Context", sourceGen, data.getSubDir());
            contexts.put(contextName, genFile);
            JavaClassSource codeGenerated = genFile.getFileContent();

            if (!pkgDestiny.endsWith(data.getSubDir())) {
                pkgDestiny += "." + data.getSubDir();

            }

            codeGenerated.setPackage(pkgDestiny);
            codeGenerated.addMethod()
                    .setName(contextName + "Context")
                    .setPublic()
                    .setConstructor(true)
                    .setParameters(String.format("int totalComponents, int startCreationIndex, ContextInfo contextInfo, EntityBaseFactory<%1$sEntity> factoryMethod", contextName))
                    .setBody("super(totalComponents, startCreationIndex, contextInfo, factoryMethod);");
            codeGenerated.addImport("com.ilargia.games.entitas.api.*");

            if (isUnique(data)) {
                addContextMethods(contextName, data, codeGenerated);
            }
        }
    }

    private void addContextMethods(String contextName, ComponentData data, JavaClassSource codeGenerated) {
        addImports(getMemberData(data), codeGenerated);
        addContextGetMethods(contextName, data, codeGenerated);
        addContextHasMethods(contextName, data, codeGenerated);
        addContextAddMethods(contextName, data, codeGenerated);
        addContextReplaceMethods(contextName, data, codeGenerated);
        addContextRemoveMethods(contextName, data, codeGenerated);
    }

    private void addImports(List<FieldSource<JavaClassSource>> memberInfos, JavaClassSource source) {
        for (FieldSource<JavaClassSource> info : memberInfos) {
            if (info.getOrigin().getImport(info.getType().toString()) != null) {
                if (source.getImport(info.getType().toString()) == null) {
                    source.addImport(info.getType());
                }
            }
        }
    }

    private void addContextGetMethods(String contextName, ComponentData data, JavaClassSource source) {
        source.addMethod()
                .setName(String.format("get%1$sEntity", getTypeName(data)))
                .setReturnType(contextName + "Entity")
                .setPublic()
                .setBody(String.format("return getGroup(%1$sMatcher.%2$s()).getSingleEntity();"
                        , WordUtils.capitalize(contextName), getTypeName(data)));

        if (!isUnique(data)) {
            source.addMethod()
                    .setName(String.format("get%1$s", getTypeName(data)))
                    .setReturnType(getTypeName(data))
                    .setPublic()
                    .setBody(String.format("return get%1$sEntity().get%1$s();"
                            , getTypeName(data)));

        }
    }

    private void addContextHasMethods(String contextName, ComponentData data, JavaClassSource source) {
        if (isUnique(data)) {
            source.addMethod()
                    .setName(String.format("is%1$s", getTypeName(data)))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return get%1$sEntity() != null;",
                            getTypeName(data)));

            source.addMethod()
                    .setName(String.format("set%1$s", getTypeName(data)))
                    .setReturnType(contextName + "Context")
                    .setPublic()
                    .setParameters("boolean value")
                    .setBody(String.format("%2$sEntity entity = get%1$sEntity();\n" +
                            "        if(value != (entity != null)) {\n" +
                            "             if(value) {\n" +
                            "                  createEntity().set%1$s(true);\n" +
                            "             } else {\n" +
                            "                  destroyEntity(entity);\n" +
                            "             }\n" +
                            "        }\n return this;", getTypeName(data), contextName));


        } else {
            source.addMethod()
                    .setName(String.format("has%1$s", getTypeName(data)))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return get%1$sEntity() != null; ",
                            getTypeName(data)));

        }
    }

    private void addContextAddMethods(String contextName, ComponentData data, JavaClassSource source) {
        if (!isUnique(data)) {
            List<MethodSource<JavaClassSource>> constructors = getConstructorData(data);
            source.addMethod()
                    .setName(String.format("set%1$s", getTypeName(data)))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(constructors != null && constructors.size() > 0
                            ? memberNamesWithTypeFromConstructor(constructors.get(0))
                            : memberNamesWithType(getMemberData(data)))
                    .setBody(String.format("if(has%1$s()) {\n" +
                                    "            throw new EntitasException(\"Could not set %1$s!\" + this + \" already has an entity with %1$s!\", " +
                                    "\"You should check if the context already has a %1$sEntity before setting it or use context.Replace%1$s().\");" +
                                    "         }\n" +
                                    "         %3$sEntity entity = createEntity();\n" +
                                    "         entity.add%1$s(%2$s);\n" +
                                    "         return entity;"
                            , getTypeName(data),
                            constructors != null && constructors.size() > 0
                                    ? memberNamesFromConstructor(constructors.get(0))
                                    : memberNames(getMemberData(data))
                            , contextName));

            source.addImport(getFullTypeName(data) );

        }
    }

    private void addContextReplaceMethods(String contextName, ComponentData data, JavaClassSource source) {
        if (!isUnique(data)) {
            List<MethodSource<JavaClassSource>> constructors = getConstructorData(data);
            source.addMethod()
                    .setName(String.format("replace%1$s", getTypeName(data)))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(constructors != null && constructors.size() > 0
                            ? memberNamesWithTypeFromConstructor(constructors.get(0))
                            : memberNamesWithType(getMemberData(data)))
                    .setBody(String.format("%3$sEntity entity = get%1$sEntity();" +
                                    "         if(entity == null) {" +
                                    "            entity = set%1$s(%2$s);" +
                                    "         } else { " +
                                    "           entity.replace%1$s(%2$s);" +
                                    "         }" +
                                    "         return entity;"
                            , getTypeName(data), constructors != null && constructors.size() > 0
                                    ? memberNamesFromConstructor(constructors.get(0))
                                    : memberNames(getMemberData(data))
                            , contextName));


        }
    }

    private void addContextRemoveMethods(String contextName, ComponentData data, JavaClassSource source) {
        if (!isUnique(data)) {
            source.addMethod()
                    .setName(String.format("remove%1$s", getTypeName(data)))
                    .setReturnType(contextName + "Context")
                    .setPublic()
                    .setBody(String.format("destroyEntity(get%1$sEntity()); return this;"
                            , getTypeName(data), memberNames(getMemberData(data))));

        }

    }

    public String memberNamesWithType(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(data -> data.getType() + " " + data.getName())
                .collect(Collectors.joining(", "));


    }

    public String memberNames(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(data -> data.getName())
                .collect(Collectors.joining(", "));
    }

    public String memberNamesFromConstructor(MethodSource<JavaClassSource> constructor) {
        return constructor.getParameters().stream()
                .map(data -> data.getName())
                .collect(Collectors.joining(", "));
    }

    public String memberNamesWithTypeFromConstructor(MethodSource<JavaClassSource> constructor) {
        return constructor.getParameters().stream()
                .map(data -> data.toString())
                .collect(Collectors.joining(", "));
    }

}
