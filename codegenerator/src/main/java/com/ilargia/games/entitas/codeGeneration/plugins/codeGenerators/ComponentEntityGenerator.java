package com.ilargia.games.entitas.codeGeneration.plugins.codeGenerators;

import com.ilargia.games.entitas.codeGeneration.CodeGenFile;
import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;
import com.ilargia.games.entitas.codeGeneration.SourceDataFile;
import com.ilargia.games.entitas.codeGeneration.StoreCodeGenerator;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsComponentDataProvider;
import com.ilargia.games.entitas.codeGenerator.CodeGeneratorOld;
import com.ilargia.games.entitas.codeGenerator.interfaces.configuration.IConfigurable;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.*;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ComponentEntityGenerator implements ICodeGenerator, IConfigurable {

    private final StoreCodeGenerator generatedFiles;

    public ComponentEntityGenerator(StoreCodeGenerator generatedFiles) {
        this.generatedFiles = generatedFiles;
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
//        Map<String, List<CodeGeneratorData>> mapContextsComponents = CodeGeneratorOld.generateMap(infos);
//        List<JavaClassSource> result = new ArrayList<>();
//
//        result.addAll((List) mapContextsComponents.keySet().stream()
//                .map(contextName -> generateEntity(contextName, mapContextsComponents.get(contextName), pkgDestiny)
//                ).collect(Collectors.toList()));
//
//        return result;
        return data.stream()
                .filter(sc -> sc instanceof SourceDataFile)
                .map(sc -> (SourceDataFile) sc)
                .filter(d -> ShouldGenerateMethodsComponentDataProvider.shouldGenerateMethods(d))
                .map(d -> generateExtensions(d))
                .ToArray();
    }

    private JavaClassSource generateEntity(SourceDataFile data, String pkgDestiny) {

    }

    private CodeGenFile generateEntity(SourceDataFile data, String pkgDestiny, String contextName) {

        JavaClassSource entityClass = Roaster.parse(JavaClassSource.class,
                String.format("public class %1$sEntity extends Entity {}", data.source.getName()));

//        if (infos.size() > 0 && !pkgDestiny.endsWith(infos.get(0).directory)) {
//            pkgDestiny += "." + infos.get(0).directory;
//
//        }
        entityClass.setPackage(pkgDestiny);
        entityClass.addMethod()
                .setName(contextName + "Entity")
                .setPublic()
                .setConstructor(true)
                .setBody("");
        entityClass.addImport("com.ilargia.games.entitas.api.*");
        entityClass.addImport("com.ilargia.games.entitas.Entity");
        entityClass.addImport("java.util.Stack");


        if (ShouldGenerateMethodsComponentDataProvider.shouldGenerateMethods(data)) {
            addEntityMethods(contextName, data, entityClass);
        }


        //System.out.println(Roaster.format(entityClass.toString()));
        return new CodeGenFile(entityClass.getName(), entityClass);
    }


    private void addEntityMethods(String contextName, CodeGeneratorData CodeGeneratorData, JavaClassSource source) {
        createGetMethods(CodeGeneratorData, source);
        createHasMethods(contextName, CodeGeneratorData, source);
        createAddMethods(contextName, CodeGeneratorData, source);
        createReplaceMethods(contextName, CodeGeneratorData, source);
        createRemoveMethods(contextName, CodeGeneratorData, source);
        createImports(CodeGeneratorData, source);

    }

    private void createGetMethods(CodeGeneratorData info, JavaClassSource source) {
        source.addImport(info.fullTypeName);


        String method = "";

        if (info.isSingletonComponent) {
            source.addField()
                    .setName(info.typeName + CodeGeneratorOld.COMPONENT_SUFFIX)
                    .setType(info.typeName)
                    .setLiteralInitializer(String.format("new %1$s();", info.typeName))
                    .setPublic();

        } else {
            MethodSource<JavaClassSource> methodGET = source.addMethod()
                    .setName(String.format("get%1$s", info.typeName))
                    .setReturnType(info.typeName)
                    .setPublic();

            String typeName = info.typeName;
            if (info.generics != null && info.generics.size() > 0) {
                typeName += "<";
                for (TypeVariableSource<JavaClassSource> generic : info.generics) {
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
                    , typeName, CodeGeneratorOld.capitalize(info.contexts.get(0)) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG, info.typeName));
        }
    }

    private void createHasMethods(String contextName, CodeGeneratorData info, JavaClassSource source) {
        source.addImport(info.fullTypeName);

        if (info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("is%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            CodeGeneratorOld.capitalize(info.contexts.get(0)) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));

            source.addMethod()
                    .setName(String.format("set%1$s", info.typeName))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters("boolean value")
                    .setBody(String.format(" if(value != hasComponent(%1$s.%2$s)) {\n" +
                                    "                    if(value) {\n" +
                                    "                        addComponent(%1$s.%2$s, %3$s);\n" +
                                    "                    } else {\n" +
                                    "                        removeComponent(%1$s.%2$s);\n" +
                                    "                    }\n" +
                                    "                }\n return this;", CodeGeneratorOld.capitalize(info.contexts.get(0)) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName, info.nameComponent));


        } else {
            source.addMethod()
                    .setName(String.format("has%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            CodeGeneratorOld.capitalize(info.contexts.get(0)) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));

        }
    }


    public void createImports(CodeGeneratorData CodeGeneratorData, JavaClassSource source) {
        if (CodeGeneratorData.imports != null) {
            for (Import imp : CodeGeneratorData.imports) {
                if (!imp.getQualifiedName().equals("com.ilargia.games.entitas.codeGenerators.Component")) {
                    source.addImport(imp);
                }
            }
        }
    }

    private void createAddMethods(String contextName, CodeGeneratorData info, JavaClassSource source) {
        if (!info.isSingletonComponent) {

            MethodSource<JavaClassSource> addMethod = source.addMethod()
                    .setName(String.format("add%1$s", info.typeName))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(info.constructores != null && info.constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(info.constructores.get(0))
                            : memberNamesWithType(info.memberInfos));

            String typeName = info.typeName;
            if (info.generics != null && info.generics.size() > 0) {
                typeName += "<";
                for (TypeVariableSource<JavaClassSource> generic : info.generics) {
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

            if (info.constructores != null && info.constructores.size() > 0) {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%5$s);\n if(component == null) { " +
                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} addComponent(%1$s.%5$s, component);\n return this;",
                        CodeGeneratorOld.capitalize(info.contexts.get(0)) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, bodyFromConstructor(info.constructores.get(0)), memberNamesFromConstructor(info.constructores.get(0))
                        , info.typeName);

            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n } \n%3$s\n addComponent(%1$s.%2$s, component);\n return this;",
                        CodeGeneratorOld.capitalize(info.contexts.get(0)) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, memberAssignments(info.memberInfos), info.typeName);
            }
            addMethod.setBody(method);

        }
    }


    private void createReplaceMethods(String contextName, CodeGeneratorData info, JavaClassSource source) {
        if (!info.isSingletonComponent) {

            MethodSource<JavaClassSource> replaceMethod = source.addMethod()
                    .setName(String.format("replace%1$s", info.typeName))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(info.constructores != null && info.constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(info.constructores.get(0))
                            : memberNamesWithType(info.memberInfos));

            String typeName = info.typeName;
            if (info.generics != null && info.generics.size() > 0) {
                typeName += "<";
                for (TypeVariableSource<JavaClassSource> generic : info.generics) {
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
            if (info.constructores != null && info.constructores.size() > 0) {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%5$s);\n if(component == null) { " +
                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} replaceComponent(%1$s.%5$s, component);\n return this;"
                        , CodeGeneratorOld.capitalize(info.contexts.get(0)) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, bodyFromConstructor(info.constructores.get(0)), memberNamesFromConstructor(info.constructores.get(0))
                        , info.typeName);
            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n} %3$s\n replaceComponent(%1$s.%2$s, component);\n return this;",
                        CodeGeneratorOld.capitalize(info.contexts.get(0)) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, memberAssignments(info.memberInfos), info.typeName);
            }
            replaceMethod.setBody(method);

        }
    }

    private void createRemoveMethods(String contextName, CodeGeneratorData info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "removeComponent(%1$s.%2$s);return this;";
            source.addMethod()
                    .setName(String.format("remove%1$s", info.typeName))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setBody(String.format(method,
                            CodeGeneratorOld.capitalize(info.contexts.get(0)) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));


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


    private JavaClassSource generateMatchers(String contextName, List<CodeGeneratorData> CodeGeneratorDatas, String pkgDestiny) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                CodeGeneratorOld.capitalize(contextName) + "Matcher"));
        javaClass.setPackage(pkgDestiny);
        //javaClass.addImport("com.ilargia.games.entitas.interfaces.IMatcher");
        javaClass.addImport("com.ilargia.games.entitas.matcher.Matcher");

        for (CodeGeneratorData info : CodeGeneratorDatas) {
            addMatcher(contextName, info, javaClass);
            addMatcherMethods(contextName, info, javaClass);
        }
        System.out.println(javaClass);
        return javaClass;
    }


    private JavaClassSource addMatcher(String contextName, CodeGeneratorData info, JavaClassSource javaClass) {
        javaClass.addField()
                .setName("_matcher" + info.typeName)
                .setType("Matcher")
                .setPrivate()
                .setStatic(true);
        return null;
    }

    private void addMatcherMethods(String contextName, CodeGeneratorData info, JavaClassSource javaClass) {
        String body = "if (_matcher%2$s == null) {" +
                "   Matcher matcher = (Matcher)Matcher.AllOf(%1$s.%2$s);" +
                "   matcher.componentNames = %1$s.componentNames();" +
                "   _matcher%2$s = matcher;" +
                "}" +
                "return _matcher%2$s;";

        javaClass.addMethod()
                .setName(info.typeName)
                .setReturnType("Matcher")
                .setPublic()
                .setStatic(true)
                .setBody(String.format(body, CodeGeneratorOld.capitalize(contextName) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                        info.typeName));

    }

}
