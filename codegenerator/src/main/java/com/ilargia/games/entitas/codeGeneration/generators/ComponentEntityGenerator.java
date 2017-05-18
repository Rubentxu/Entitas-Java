package com.ilargia.games.entitas.codeGeneration.generators;

import com.ilargia.games.entitas.codeGeneration.data.CodeGenFile;
import com.ilargia.games.entitas.codeGeneration.data.MemberData;
import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile;
import com.ilargia.games.entitas.codeGeneration.data.StoreCodeGenerator;
import com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers.ShouldGenerateMethodsDataProvider;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGeneration.interfaces.IConfigurable;
import com.ilargia.games.entitas.codeGenerator.CodeGeneratorOld;
import org.apache.commons.lang3.text.WordUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers.ConstructorDataProvider.getConstructorData;
import static com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers.ContextsDataProvider.getContextNames;
import static com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers.GenericsDataProvider.getGenericsData;
import static com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers.MemberDataProvider.getMemberData;

public class ComponentEntityGenerator implements ICodeGenerator, IConfigurable {

    public static final String COMPONENT_SUFFIX = "Component";
    public static final String DEFAULT_COMPONENT_LOOKUP_TAG = "ComponentsLookup";
    
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
    public List<CodeGenFile> generate(List<SourceDataFile> datas, String pkgDestiny) {
        Map<String, List<SourceDataFile>> mapContextsComponents = generateMap(datas);

        return mapContextsComponents.keySet().stream()
                .map(contextName -> generateEntities(mapContextsComponents.get(contextName), pkgDestiny, contextName))
                .flatMap(list-> list.stream())
                .collect(Collectors.toList());

    }

    public static Map<String, List<SourceDataFile>> generateMap(List<SourceDataFile> datas) {
        Map<String, List<SourceDataFile>> contextComponents = new HashMap<>();
        datas.sort((c1, c2) -> c1.source.getName().compareTo(c2.source.getName()));

        for (SourceDataFile data : datas) {
            for (String contextName :getContextNames(data)) {
                if (!contextComponents.containsKey(contextName)) {
                    contextComponents.put(contextName, new ArrayList<>());
                }
                List<SourceDataFile> list = contextComponents.get(contextName);
                list.add(data);
            }
        }

//        for (List<SourceDataFile> infos : contextComponents.values()) {
//            int index = 0;
//            for (ComponentInfo info : infos) {
//                info.index = index++;
//                info.totalComponents = infos.size();
//            }
//        }
        return contextComponents;

    }

    private List<CodeGenFile> generateEntities(List<SourceDataFile> datas, String pkgDestiny, String contextName) {
        return datas.stream()
                .filter(d -> ShouldGenerateMethodsDataProvider.shouldGenerateMethods(d))
                .map(d -> generateEntity(d, pkgDestiny, contextName))
                .collect(Collectors.toList());

    }

    private CodeGenFile generateEntity(SourceDataFile data, String pkgDestiny, String contextName) {

        JavaClassSource gen = Roaster.parse(JavaClassSource.class,
                String.format("public class %1$sEntity extends Entity {}", data.source.getName()));

//        if (infos.size() > 0 && !pkgDestiny.endsWith(infos.get(0).directory)) {
//            pkgDestiny += "." + infos.get(0).directory;
//
//        }
        gen.setPackage(pkgDestiny);
        gen.addMethod()
                .setName(contextName + "Entity")
                .setPublic()
                .setConstructor(true)
                .setBody("");
        gen.addImport("com.ilargia.games.entitas.api.*");
        gen.addImport("com.ilargia.games.entitas.Entity");
        gen.addImport("java.util.Stack");


        if (ShouldGenerateMethodsDataProvider.shouldGenerateMethods(data)) {
            addEntityMethods(contextName, data, gen);
        }


        //System.out.println(Roaster.format(entityClass.toString()));
        return new CodeGenFile(gen.getName(),null,null, gen);
    }


    private void addEntityMethods(String contextName, SourceDataFile codeGeneratorData, JavaClassSource source) {
        createGetMethods(contextName, codeGeneratorData, source);
        createHasMethods(contextName, codeGeneratorData, source);
        createAddMethods(contextName, codeGeneratorData, source);
        createReplaceMethods(contextName, codeGeneratorData, source);
        createRemoveMethods(contextName, codeGeneratorData, source);
        createImports(codeGeneratorData, source);

    }

    private void createGetMethods(String contextName, SourceDataFile data, JavaClassSource gen) {
        gen.addImport(data.source.getCanonicalName());

        String method = "";

        if (data.source.getFields().size() == 0) {
            gen.addField()
                    .setName(data.source.getName() + CodeGeneratorOld.COMPONENT_SUFFIX)
                    .setType(data.source.getName())
                    .setLiteralInitializer(String.format("new %1$s();", data.source.getName()))
                    .setPublic();

        } else {
            MethodSource<JavaClassSource> methodGET = gen.addMethod()
                    .setName(String.format("get%1$s", data.source.getName()))
                    .setReturnType(data.source.getName())
                    .setPublic();


            String typeName = data.source.getName();
            List<TypeVariableSource<JavaClassSource>> generics = data.source.getOrigin().getTypeVariables();
            if (generics != null && generics.size() > 0) {
                typeName += "<";
                for (TypeVariableSource<JavaClassSource> generic : generics) {
                    String javaType[] = new String[generic.getBounds().size()];
                    for (int i = 0; i < generic.getBounds().size(); i++) {
                        javaType[i] = (String) generic.getBounds().get(i).getSimpleName();
                    }
                    methodGET.addTypeVariable().setName(generic.getName()).setBounds(javaType);
                    if (gen.getName().indexOf("<") != gen.getName().length() - 1) typeName += ",";
                    typeName += generic.getName();
                }
                typeName += ">";

            }
            methodGET.setBody(String.format("return (%1$s)getComponent(%2$s.%3$s);"
                    , gen.getName(), WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG, data.source.getName()));
        }
    }

    private void createHasMethods(String contextName, SourceDataFile data, JavaClassSource gen) {
        gen.addImport(data.source.getCanonicalName());

        if (data.source.getFields().size() == 0) {
            gen.addMethod()
                    .setName(String.format("is%1$s", data.source.getName()))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                            data.source.getName()));

            gen.addMethod()
                    .setName(String.format("set%1$s", data.source.getName()))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters("boolean value")
                    .setBody(String.format(" if(value != hasComponent(%1$s.%2$s)) {\n" +
                                    "                    if(value) {\n" +
                                    "                        addComponent(%1$s.%2$s, %3$s);\n" +
                                    "                    } else {\n" +
                                    "                        removeComponent(%1$s.%2$s);\n" +
                                    "                    }\n" +
                                    "                }\n return this;", WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                            data.source.getName(), nameComponent(data.source.getName())));


        } else {
            gen.addMethod()
                    .setName(String.format("has%1$s", data.source.getName()))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                            data.source.getName()));

        }
    }


    public void createImports(SourceDataFile data, JavaClassSource source) {
        if (data.source.getImports() != null) {
            for (Import imp : data.source.getImports()) {
                if (!imp.getQualifiedName().equals("com.ilargia.games.entitas.generators.Component")) {
                    source.addImport(imp);
                }
            }
        }
    }

    private void createAddMethods(String contextName, SourceDataFile data, JavaClassSource source) {
        if (data.source.getFields().size() != 0) {

            List<MethodSource<JavaClassSource>> constructores = getConstructorData(data);

            MethodSource<JavaClassSource> addMethod = source.addMethod()
                    .setName(String.format("add%1$s", data.source.getName()))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(constructores != null && constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(constructores.get(0))
                            : memberNamesWithType(getMemberData(data)));

            String typeName = data.source.getName();
            List<TypeVariableSource<JavaClassSource>> generics = getGenericsData(data);

            if (generics != null && generics.size() > 0) {
                typeName += "<";
                for (TypeVariableSource<JavaClassSource> generic : generics) {
                    String javaType[] = new String[generic.getBounds().size()];
                    for (int i = 0; i < generic.getBounds().size(); i++) {
                        javaType[i] = (String) generic.getBounds().get(i).getSimpleName();
                    }
                    addMethod.addTypeVariable().setName(generic.getName()).setBounds(javaType);
                    if (source.getName().indexOf("<") != source.getName().length() - 1) typeName += ",";
                    typeName += generic.getName();
                }
                typeName += ">";
            }
            String method = "";

            if (constructores != null && constructores.size() > 0) {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%5$s);\n if(component == null) { " +
                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} addComponent(%1$s.%5$s, component);\n return this;",
                        WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        source.getName(), bodyFromConstructor(constructores.get(0)), memberNamesFromConstructor(constructores.get(0))
                        , data.source.getName());

            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n } \n%3$s\n addComponent(%1$s.%2$s, component);\n return this;",
                        WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        source.getName(), memberAssignments(getMemberData(data)), data.source.getName());
            }
            addMethod.setBody(method);

        }
    }


    private void createReplaceMethods(String contextName, SourceDataFile data, JavaClassSource source) {
        if (data.source.getFields().size() != 0) {
            List<MethodSource<JavaClassSource>> constructores = getConstructorData(data);

            MethodSource<JavaClassSource> replaceMethod = source.addMethod()
                    .setName(String.format("replace%1$s", data.source.getName()))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(constructores != null && constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(constructores.get(0))
                            : memberNamesWithType(getMemberData(data)));

            String typeName = data.source.getName();
            List<TypeVariableSource<JavaClassSource>> generics = getGenericsData(data);

            if (generics != null && generics.size() > 0) {
                typeName += "<";
                for (TypeVariableSource<JavaClassSource> generic : generics) {
                    String javaType[] = new String[generic.getBounds().size()];
                    for (int i = 0; i < generic.getBounds().size(); i++) {
                        javaType[i] = (String) generic.getBounds().get(i).getSimpleName();
                    }
                    replaceMethod.addTypeVariable().setName(generic.getName()).setBounds(javaType);
                    if (source.getName().indexOf("<") != source.getName().length() - 1) typeName += ",";
                    typeName += generic.getName();
                }
                typeName += ">";
            }
            String method;
            if (constructores != null && constructores.size() > 0) {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%5$s);\n if(component == null) { " +
                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} replaceComponent(%1$s.%5$s, component);\n return this;"
                        , WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        source.getName(), bodyFromConstructor(constructores.get(0)), memberNamesFromConstructor(constructores.get(0))
                        , data.source.getName());
            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n} %3$s\n replaceComponent(%1$s.%2$s, component);\n return this;",
                        WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        source.getName(), memberAssignments(getMemberData(data)), data.source.getName());
            }
            replaceMethod.setBody(method);

        }
    }

    private void createRemoveMethods(String contextName, SourceDataFile data, JavaClassSource source) {
        if (data.source.getFields().size() != 0) {
            String method = "removeComponent(%1$s.%2$s);return this;";
            source.addMethod()
                    .setName(String.format("remove%1$s", data.source.getName()))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setBody(String.format(method,
                            WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                            data.source.getName()));


        }
    }

    public String memberNamesWithType(List<MemberData> memberInfos) {
        return memberInfos.stream()
                .map(info -> info.type.getName() + " " + info.name)
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

    public String memberAssignments(List<MemberData> memberInfos) {
        String format = "component.%1$s = %2$s;";
        return memberInfos.stream().map(
                info -> {
                    String newArg = info.name;
                    return String.format(format, info.name, newArg);
                }
        ).collect(Collectors.joining("\n"));

    }


    private JavaClassSource generateMatchers(String contextName, List<SourceDataFile> sourceDataFiles, String pkgDestiny) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                WordUtils.capitalize(contextName) + "Matcher"));
        javaClass.setPackage(pkgDestiny);
        //javaClass.addImport("com.ilargia.games.entitas.interfaces.IMatcher");
        javaClass.addImport("com.ilargia.games.entitas.matcher.Matcher");

        for (SourceDataFile info : sourceDataFiles) {
            addMatcher(contextName, info, javaClass);
            addMatcherMethods(contextName, info, javaClass);
        }
        System.out.println(javaClass);
        return javaClass;
    }


    private JavaClassSource addMatcher(String contextName, SourceDataFile info, JavaClassSource javaClass) {
        javaClass.addField()
                .setName("_matcher" + info.source.getName())
                .setType("Matcher")
                .setPrivate()
                .setStatic(true);
        return null;
    }

    private void addMatcherMethods(String contextName, SourceDataFile info, JavaClassSource javaClass) {
        String body = "if (_matcher%2$s == null) {" +
                "   Matcher matcher = (Matcher)Matcher.AllOf(%1$s.%2$s);" +
                "   matcher.componentNames = %1$s.componentNames();" +
                "   _matcher%2$s = matcher;" +
                "}" +
                "return _matcher%2$s;";

        javaClass.addMethod()
                .setName(info.source.getName())
                .setReturnType("Matcher")
                .setPublic()
                .setStatic(true)
                .setBody(String.format(body, WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        info.source.getName()));

    }

    private String nameComponent(String name) {
        return (name.contains(COMPONENT_SUFFIX))
                ? name
                : name + COMPONENT_SUFFIX;
    }

}
