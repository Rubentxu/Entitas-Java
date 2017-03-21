package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityGenerator implements IComponentCodeGenerator {


    @Override
    public List<JavaClassSource> generate(List<ComponentInfo> infos, String pkgDestiny) {
        Map<String, List<ComponentInfo>> mapContextsComponents = CodeGenerator.generateMap(infos);
        List<JavaClassSource> result = new ArrayList<>();

        result.addAll((List) mapContextsComponents.keySet().stream()
                .map(contextName -> generateEntity(contextName, mapContextsComponents.get(contextName), pkgDestiny)
                ).collect(Collectors.toList()));

        return result;
    }

    private JavaClassSource generateEntity(String contextName, List<ComponentInfo> infos, String pkgDestiny) {

        JavaClassSource entityClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$sEntity extends Entity {}", contextName));

        if (infos.size() > 0 && !pkgDestiny.endsWith(infos.get(0).subDir)) {
            pkgDestiny += "." + infos.get(0).subDir;

        }
        entityClass.setPackage(pkgDestiny);
        entityClass.addMethod()
                .setName(contextName + "Entity")
                .setPublic()
                .setConstructor(true)
                .setBody("");
        entityClass.addImport("com.ilargia.games.entitas.api.*");
        entityClass.addImport("com.ilargia.games.entitas.Entity");
        entityClass.addImport("java.util.Stack");

        for (ComponentInfo info : infos) {
            if (info.generateMethods) {
                addImporEnums(info, entityClass);
                addEntityMethods(contextName, info, entityClass);
            }
        }

        System.out.println(Roaster.format(entityClass.toString()));
        return entityClass;
    }

    private void addImporEnums(ComponentInfo info, JavaClassSource entityClass) {
        if (info.internalEnums != null)
            info.internalEnums.stream().forEach(e -> entityClass.addImport(e));

    }

    private void addEntityMethods(String contextName, ComponentInfo componentInfo, JavaClassSource source) {
        addGetMethods(componentInfo, source);
        addHasMethods(contextName, componentInfo, source);
        addAddMethods(contextName, componentInfo, source);
        addReplaceMethods(contextName, componentInfo, source);
        addRemoveMethods(contextName, componentInfo, source);
        addImportClass(componentInfo, source);

    }

    private void addGetMethods(ComponentInfo info, JavaClassSource source) {
        source.addImport(info.fullTypeName);


        String method = "";

        if (info.isSingletonComponent) {
            source.addField()
                    .setName(info.typeName + CodeGenerator.COMPONENT_SUFFIX)
                    .setType(info.typeName)
                    .setLiteralInitializer(String.format("new %1$s();", info.typeName))
                    .setPublic();

        } else {
            MethodSource<JavaClassSource> methodGET = source.addMethod()
                    .setName(String.format("get%1$s", info.typeName))
                    .setReturnType(info.typeName)
                    .setPublic();

            String typeName = info.typeName ;
            if(info.generics !=null && info.generics.size()>0) {
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
                methodGET.setBody(String.format("return (%1$s)getComponent(%2$s.%3$s);"
                        , typeName, CodeGenerator.capitalize(info.contexts.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG, info.typeName));
            }
        }
    }

    private void addHasMethods(String contextName, ComponentInfo info, JavaClassSource source) {
        source.addImport(info.fullTypeName);

        if (info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("is%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            CodeGenerator.capitalize(info.contexts.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
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
                                    "                }\n return this;", CodeGenerator.capitalize(info.contexts.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName, info.nameComponent));


        } else {
            source.addMethod()
                    .setName(String.format("has%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            CodeGenerator.capitalize(info.contexts.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));

        }
    }


    public void addImportClass(ComponentInfo componentInfo, JavaClassSource source) {
        if (componentInfo.imports != null) {
            for (Import imp : componentInfo.imports) {
                if (!imp.getQualifiedName().equals("com.ilargia.games.entitas.codeGenerator.Component")) {
                    source.addImport(imp);
                }
            }
        }
    }

    private void addAddMethods(String contextName, ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {

            MethodSource<JavaClassSource> addMethod = source.addMethod()
                    .setName(String.format("add%1$s", info.typeName))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(info.constructores != null && info.constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(info.constructores.get(0))
                            : memberNamesWithType(info.memberInfos));

            String typeName = info.typeName ;
            if(info.generics !=null && info.generics.size()>0) {
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
                        CodeGenerator.capitalize(info.contexts.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, bodyFromConstructor(info.constructores.get(0)), memberNamesFromConstructor(info.constructores.get(0))
                        ,info.typeName);

            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n } \n%3$s\n addComponent(%1$s.%2$s, component);\n return this;",
                        CodeGenerator.capitalize(info.contexts.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, memberAssignments(info.memberInfos),info.typeName);
            }
            addMethod.setBody(method);

        }
    }


    private void addReplaceMethods(String contextName, ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {

            MethodSource<JavaClassSource> replaceMethod = source.addMethod()
                    .setName(String.format("replace%1$s", info.typeName))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(info.constructores != null && info.constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(info.constructores.get(0))
                            : memberNamesWithType(info.memberInfos));

            String typeName = info.typeName ;
            if(info.generics !=null && info.generics.size()>0) {
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
                        , CodeGenerator.capitalize(info.contexts.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, bodyFromConstructor(info.constructores.get(0)), memberNamesFromConstructor(info.constructores.get(0))
                        ,info.typeName);
            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n} %3$s\n replaceComponent(%1$s.%2$s, component);\n return this;",
                        CodeGenerator.capitalize(info.contexts.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        typeName, memberAssignments(info.memberInfos),info.typeName);
            }
            replaceMethod.setBody(method);

        }
    }

    private void addRemoveMethods(String contextName, ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "removeComponent(%1$s.%2$s);return this;";
            source.addMethod()
                    .setName(String.format("remove%1$s", info.typeName))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setBody(String.format(method,
                            CodeGenerator.capitalize(info.contexts.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
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
                .map(info ->info.toString())
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


    private JavaClassSource generateMatchers(String contextName, List<ComponentInfo> componentInfos, String pkgDestiny) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                CodeGenerator.capitalize(contextName) + "Matcher"));
        javaClass.setPackage(pkgDestiny);
        //javaClass.addImport("com.ilargia.games.entitas.interfaces.IMatcher");
        javaClass.addImport("com.ilargia.games.entitas.matcher.Matcher");

        for (ComponentInfo info : componentInfos) {
            addMatcher(contextName, info, javaClass);
            addMatcherMethods(contextName, info, javaClass);
        }
        System.out.println(javaClass);
        return javaClass;
    }


    private JavaClassSource addMatcher(String contextName, ComponentInfo info, JavaClassSource javaClass) {
        javaClass.addField()
                .setName("_matcher" + info.typeName)
                .setType("Matcher")
                .setPrivate()
                .setStatic(true);
        return null;
    }

    private void addMatcherMethods(String contextName, ComponentInfo info, JavaClassSource javaClass) {
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
                .setBody(String.format(body, CodeGenerator.capitalize(contextName) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        info.typeName));

    }


}
