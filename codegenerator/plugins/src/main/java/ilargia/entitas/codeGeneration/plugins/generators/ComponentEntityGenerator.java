package ilargia.entitas.codeGeneration.plugins.generators;

import ilargia.entitas.codeGeneration.CodeGenerator;
import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.data.MemberData;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider;
import org.apache.commons.lang3.text.WordUtils;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.getFullTypeName;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ConstructorDataProvider.getConstructorData;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsDataProvider.getContextNames;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.CustomPrefixDataProvider.getCustomComponentPrefix;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.GenericsDataProvider.getGenericsData;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.MemberDataProvider.getMemberData;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods;


public class ComponentEntityGenerator implements ICodeGenerator, IConfigurable {


    public static final String COMPONENT_SUFFIX = "Component";
    public static final String DEFAULT_COMPONENT_LOOKUP_TAG = "ComponentsLookup";
    public static final String COMPONENTS_LOOKUP = "ComponentsLookup";
    private Map<String, String> flagComponent;
    private Map<String, String> standarComponent;

    public ComponentEntityGenerator() {
        flagComponent = new HashMap<>();
        standarComponent = new HashMap<>();
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/example.twig");
        JtwigModel model = JtwigModel.newModel().with("var", "World");

        template.render(model, System.out);
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
    public Properties getDefaultProperties() {
        return null;
    }

    @Override
    public void configure(Properties properties) {

    }

    @Override
    public List<CodeGenFile> generate(List<CodeGeneratorData> data) {
        return data.stream()
                .filter(d-> d instanceof ComponentData)
                .map(d-> (ComponentData) d)
                .filter(d-> shouldGenerateMethods(d))
                .flatMap(d-> generateEntities(d).stream())
                .collect(Collectors.toList());
    }

    List<CodeGenFile> generateEntities(ComponentData data) {
        return getContextNames(data).stream()
                .map(contextName -> generateEntity(contextName, data))
                .collect(Collectors.toList());
    }

    private CodeGenFile generateEntity(String contextName, ComponentData data){
        String componentName = getFullTypeName(data);
        String index = contextName + COMPONENTS_LOOKUP + "." + componentName;
        List<MemberData> memberData = getMemberData(data);
        Map<String, String> template = memberData.size() == 0
                ? flagComponent
                : standarComponent;

                data.put("ContextName", contextName);
                data.put("ComponentType", getFullTypeName(data));
                data.put("ComponentName", componentName);
                data.put("componentName", WordUtils.uncapitalize(componentName));
                data.put("prefixedName",  WordUtils.uncapitalize(getCustomComponentPrefix(data)) + componentName);
                data.put("Index", index);
                data.put("memberArgs", getMemberArgs(memberData));
                data.put("memberAssignment", getMemberAssignment(memberData));

        return new CodeGenFile(
                contextName + Path.DirectorySeparatorChar +
                        "Components" + Path.DirectorySeparatorChar +
                        contextName + componentName.AddComponentSuffix() + ".cs",
                fileContent,
                GetType().FullName
        );
    }
//    @Override
//    public List<CodeGenFile<JavaClassSource>> generate(List<CodeGeneratorData> datas) {
//        Map<String, List<CodeGeneratorData>> mapContextsComponents = generateMap(datas);
//
//        return mapContextsComponents.keySet().stream()
//                .map(contextName -> generateEntities(mapContextsComponents.get(contextName), contextName))
//                .flatMap(list -> list.stream())
//                .collect(Collectors.toList());
//
//    }
//
//    private List<CodeGenFile> generateEntities(List<CodeGeneratorData> datas, String contextName) {
//        return datas.stream()
//                .filter(d -> ShouldGenerateMethodsDataProvider.shouldGenerateMethods(d))
//                .map(d -> generateEntity(d, contextName))
//                .collect(Collectors.toList());
//
//    }
//
//    private CodeGenFile generateEntity(SourceDataFile data, String pkgDestiny, String contextName) {
//
//        JavaClassSource gen = Roaster.parse(JavaClassSource.class,
//                String.format("public class %1$sEntity extends Entity {}", data.getFileContent().getName()));
//
////        if (infos.size() > 0 && !pkgDestiny.endsWith(infos.get(0).directory)) {
////            pkgDestiny += "." + infos.get(0).directory;
////
////        }
//        gen.setPackage(pkgDestiny);
//        gen.addMethod()
//                .setName(contextName + "Entity")
//                .setPublic()
//                .setConstructor(true)
//                .setBody("");
//        gen.addImport("com.ilargia.games.entitas.api.*");
//        gen.addImport("Entity");
//        gen.addImport("java.util.Stack");
//
//
//        if (ShouldGenerateMethodsDataProvider.shouldGenerateMethods(data)) {
//            addEntityMethods(contextName, data, gen);
//        }
//
//
//        //System.out.println(Roaster.format(entityClass.toString()));
//        return new CodeGenFile(gen.getName(), null, null, gen);
//    }
//
//
//    private void addEntityMethods(String contextName, SourceDataFile codeGeneratorData, JavaClassSource source) {
//        createGetMethods(contextName, codeGeneratorData, source);
//        createHasMethods(contextName, codeGeneratorData, source);
//        createAddMethods(contextName, codeGeneratorData, source);
//        createReplaceMethods(contextName, codeGeneratorData, source);
//        createRemoveMethods(contextName, codeGeneratorData, source);
//        createImports(codeGeneratorData, source);
//
//    }
//
//    private void createGetMethods(String contextName, SourceDataFile data, JavaClassSource gen) {
//        gen.addImport(data.getFileContent().getCanonicalName());
//
//        String method = "";
//
//        if (data.getFileContent().getFields().size() == 0) {
//            gen.addField()
//                    .setName(data.getFileContent().getName() + COMPONENT_SUFFIX)
//                    .setType(data.getFileContent().getName())
//                    .setLiteralInitializer(String.format("new %1$s();", data.getFileContent().getName()))
//                    .setPublic();
//
//        } else {
//            MethodSource<JavaClassSource> methodGET = gen.addMethod()
//                    .setName(String.format("get%1$s", data.getFileContent().getName()))
//                    .setReturnType(data.getFileContent().getName())
//                    .setPublic();
//
//
//            String typeName = data.getFileContent().getName();
//            List<TypeVariableSource<JavaClassSource>> generics = data.getFileContent().getOrigin().getTypeVariables();
//            if (generics != null && generics.size() > 0) {
//                typeName += "<";
//                for (TypeVariableSource<JavaClassSource> generic : generics) {
//                    String javaType[] = new String[generic.getBounds().size()];
//                    for (int i = 0; i < generic.getBounds().size(); i++) {
//                        javaType[i] = (String) generic.getBounds().get(i).getSimpleName();
//                    }
//                    methodGET.addTypeVariable().setName(generic.getName()).setBounds(javaType);
//                    if (gen.getName().indexOf("<") != gen.getName().length() - 1) typeName += ",";
//                    typeName += generic.getName();
//                }
//                typeName += ">";
//
//            }
//            methodGET.setBody(String.format("return (%1$s)getComponent(%2$s.%3$s);"
//                    , gen.getName(), WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG, data.getFileContent().getName()));
//        }
//    }
//
//    private void createHasMethods(String contextName, SourceDataFile data, JavaClassSource gen) {
//        gen.addImport(data.getFileContent().getCanonicalName());
//
//        if (data.getFileContent().getFields().size() == 0) {
//            gen.addMethod()
//                    .setName(String.format("is%1$s", data.getFileContent().getName()))
//                    .setReturnType("boolean")
//                    .setPublic()
//                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
//                            WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
//                            data.getFileContent().getName()));
//
//            gen.addMethod()
//                    .setName(String.format("set%1$s", data.getFileContent().getName()))
//                    .setReturnType(contextName + "Entity")
//                    .setPublic()
//                    .setParameters("boolean value")
//                    .setBody(String.format(" if(value != hasComponent(%1$s.%2$s)) {\n" +
//                                    "                    if(value) {\n" +
//                                    "                        addComponent(%1$s.%2$s, %3$s);\n" +
//                                    "                    } else {\n" +
//                                    "                        removeComponent(%1$s.%2$s);\n" +
//                                    "                    }\n" +
//                                    "                }\n return this;", WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
//                            data.getFileContent().getName(), nameComponent(data.getFileContent().getName())));
//
//
//        } else {
//            gen.addMethod()
//                    .setName(String.format("has%1$s", data.getFileContent().getName()))
//                    .setReturnType("boolean")
//                    .setPublic()
//                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
//                            WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
//                            data.getFileContent().getName()));
//
//        }
//    }
//
//
//    public void createImports(SourceDataFile data, JavaClassSource source) {
//        if (data.getFileContent().getImports() != null) {
//            for (Import imp : data.getFileContent().getImports()) {
//                if (!imp.getQualifiedName().equals("com.ilargia.games.entitas.generators.Component")) {
//                    source.addImport(imp);
//                }
//            }
//        }
//    }
//
//    private void createAddMethods(String contextName, SourceDataFile data, JavaClassSource source) {
//        if (data.getFileContent().getFields().size() != 0) {
//
//            List<MethodSource<JavaClassSource>> constructores = getConstructorData(data);
//
//            MethodSource<JavaClassSource> addMethod = source.addMethod()
//                    .setName(String.format("add%1$s", data.getFileContent().getName()))
//                    .setReturnType(contextName + "Entity")
//                    .setPublic()
//                    .setParameters(constructores != null && constructores.size() > 0
//                            ? memberNamesWithTypeFromConstructor(constructores.get(0))
//                            : memberNamesWithType(getMemberData(data)));
//
//            String typeName = data.getFileContent().getName();
//            List<TypeVariableSource<JavaClassSource>> generics = getGenericsData(data);
//
//            if (generics != null && generics.size() > 0) {
//                typeName += "<";
//                for (TypeVariableSource<JavaClassSource> generic : generics) {
//                    String javaType[] = new String[generic.getBounds().size()];
//                    for (int i = 0; i < generic.getBounds().size(); i++) {
//                        javaType[i] = (String) generic.getBounds().get(i).getSimpleName();
//                    }
//                    addMethod.addTypeVariable().setName(generic.getName()).setBounds(javaType);
//                    if (source.getName().indexOf("<") != source.getName().length() - 1) typeName += ",";
//                    typeName += generic.getName();
//                }
//                typeName += ">";
//            }
//            String method = "";
//
//            if (constructores != null && constructores.size() > 0) {
//                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%5$s);\n if(component == null) { " +
//                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} addComponent(%1$s.%5$s, component);\n return this;",
//                        WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
//                        source.getName(), bodyFromConstructor(constructores.get(0)), memberNamesFromConstructor(constructores.get(0))
//                        , data.getFileContent().getName());
//
//            } else {
//                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
//                                "component = new %2$s();\n } \n%3$s\n addComponent(%1$s.%2$s, component);\n return this;",
//                        WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
//                        source.getName(), memberAssignments(getMemberData(data)), data.getFileContent().getName());
//            }
//            addMethod.setBody(method);
//
//        }
//    }
//
//
//    private void createReplaceMethods(String contextName, SourceDataFile data, JavaClassSource source) {
//        if (data.getFileContent().getFields().size() != 0) {
//            List<MethodSource<JavaClassSource>> constructores = getConstructorData(data);
//
//            MethodSource<JavaClassSource> replaceMethod = source.addMethod()
//                    .setName(String.format("replace%1$s", data.getFileContent().getName()))
//                    .setReturnType(contextName + "Entity")
//                    .setPublic()
//                    .setParameters(constructores != null && constructores.size() > 0
//                            ? memberNamesWithTypeFromConstructor(constructores.get(0))
//                            : memberNamesWithType(getMemberData(data)));
//
//            String typeName = data.getFileContent().getName();
//            List<TypeVariableSource<JavaClassSource>> generics = getGenericsData(data);
//
//            if (generics != null && generics.size() > 0) {
//                typeName += "<";
//                for (TypeVariableSource<JavaClassSource> generic : generics) {
//                    String javaType[] = new String[generic.getBounds().size()];
//                    for (int i = 0; i < generic.getBounds().size(); i++) {
//                        javaType[i] = (String) generic.getBounds().get(i).getSimpleName();
//                    }
//                    replaceMethod.addTypeVariable().setName(generic.getName()).setBounds(javaType);
//                    if (source.getName().indexOf("<") != source.getName().length() - 1) typeName += ",";
//                    typeName += generic.getName();
//                }
//                typeName += ">";
//            }
//            String method;
//            if (constructores != null && constructores.size() > 0) {
//                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%5$s);\n if(component == null) { " +
//                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} replaceComponent(%1$s.%5$s, component);\n return this;"
//                        , WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
//                        source.getName(), bodyFromConstructor(constructores.get(0)), memberNamesFromConstructor(constructores.get(0))
//                        , data.getFileContent().getName());
//            } else {
//                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
//                                "component = new %2$s();\n} %3$s\n replaceComponent(%1$s.%2$s, component);\n return this;",
//                        WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
//                        source.getName(), memberAssignments(getMemberData(data)), data.getFileContent().getName());
//            }
//            replaceMethod.setBody(method);
//
//        }
//    }
//
//    private void createRemoveMethods(String contextName, SourceDataFile data, JavaClassSource source) {
//        if (data.getFileContent().getFields().size() != 0) {
//            String method = "removeComponent(%1$s.%2$s);return this;";
//            source.addMethod()
//                    .setName(String.format("remove%1$s", data.getFileContent().getName()))
//                    .setReturnType(contextName + "Entity")
//                    .setPublic()
//                    .setBody(String.format(method,
//                            WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
//                            data.getFileContent().getName()));
//
//
//        }
//    }
//
//    public String memberNamesWithType(List<MemberData> memberInfos) {
//        return memberInfos.stream()
//                .map(info -> info.type.getName() + " " + info.name)
//                .collect(Collectors.joining(", "));
//
//    }
//
//
//    public String memberNames(List<FieldSource<JavaClassSource>> memberInfos) {
//        return memberInfos.stream()
//                .map(info -> info.getName())
//                .collect(Collectors.joining(", "));
//    }
//
//    public String memberNamesFromConstructor(MethodSource<JavaClassSource> constructor) {
//        return constructor.getParameters().stream()
//                .map(info -> info.getName())
//                .collect(Collectors.joining(", "));
//    }
//
//    public String memberNamesWithTypeFromConstructor(MethodSource<JavaClassSource> constructor) {
//        return constructor.getParameters().stream()
//                .map(info -> info.toString())
//                .collect(Collectors.joining(", "));
//    }
//
//    public String bodyFromConstructor(MethodSource<JavaClassSource> constructor) {
//        return constructor.getBody().replaceAll("this", "component");
//    }
//
//    public String memberAssignments(List<MemberData> memberInfos) {
//        String format = "component.%1$s = %2$s;";
//        return memberInfos.stream().map(
//                info -> {
//                    String newArg = info.name;
//                    return String.format(format, info.name, newArg);
//                }
//        ).collect(Collectors.joining("\n"));
//
//    }
//
//
//    private JavaClassSource generateMatchers(String contextName, List<SourceDataFile> sourceDataFiles, String pkgDestiny) {
//        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
//                WordUtils.capitalize(contextName) + "Matcher"));
//        javaClass.setPackage(pkgDestiny);
//        //javaClass.addImport("com.ilargia.games.entitas.interfaces.IMatcher");
//        javaClass.addImport("Matcher");
//
//        for (SourceDataFile info : sourceDataFiles) {
//            addMatcher(contextName, info, javaClass);
//            addMatcherMethods(contextName, info, javaClass);
//        }
//        System.out.println(javaClass);
//        return javaClass;
//    }
//
//
//    private JavaClassSource addMatcher(String contextName, SourceDataFile info, JavaClassSource javaClass) {
//        javaClass.addField()
//                .setName("_matcher" + info.getFileContent().getName())
//                .setType("Matcher")
//                .setPrivate()
//                .setStatic(true);
//        return null;
//    }
//
//    private void addMatcherMethods(String contextName, SourceDataFile info, JavaClassSource javaClass) {
//        String body = "if (_matcher%2$s == null) {" +
//                "   Matcher matcher = (Matcher)Matcher.AllOf(%1$s.%2$s);" +
//                "   matcher.componentNames = %1$s.componentNames();" +
//                "   _matcher%2$s = matcher;" +
//                "}" +
//                "return _matcher%2$s;";
//
//        javaClass.addMethod()
//                .setName(info.getFileContent().getName())
//                .setReturnType("Matcher")
//                .setPublic()
//                .setStatic(true)
//                .setBody(String.format(body, WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
//                        info.getFileContent().getName()));
//
//    }
//
//    private String nameComponent(String name) {
//        return (name.contains(COMPONENT_SUFFIX))
//                ? name
//                : name + COMPONENT_SUFFIX;
//    }

}
