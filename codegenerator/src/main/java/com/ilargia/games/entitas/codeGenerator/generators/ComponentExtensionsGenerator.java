package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComponentExtensionsGenerator implements IComponentCodeGenerator {


    @Override
    public List<JavaClassSource> generate(List<ComponentInfo> infos, String pkgDestiny) {
        Map<String, List<ComponentInfo>> mapPoolsComponents = CodeGenerator.generateMap(infos);

        List<JavaClassSource> result = new ArrayList<>();
        JavaClassSource entitas = Roaster.parse(JavaClassSource.class, "public class Entity extends com.ilargia.games.entitas.Entity {}");
        entitas.setPackage(pkgDestiny);

        entitas.addMethod()
                .setName("Entity")
                .setPublic()
                .setConstructor(true)
                .setParameters("int totalComponents,Stack<IComponent>[] componentPools, PoolMetaData poolMetaData")
                .setBody("super(totalComponents, componentPools, poolMetaData);");
        entitas.addImport("com.ilargia.games.entitas.PoolMetaData");
        entitas.addImport("com.ilargia.games.entitas.interfaces.IComponent");
        entitas.addImport("java.util.Stack");

        result.add(entitas);

        for (ComponentInfo info : infos) {
            if (info.generateMethods) {
                generateComponentExtension(info, entitas);
            }
        }

        result.addAll((List) mapPoolsComponents.keySet().stream()
                .map(poolName -> generateMatchers(poolName, mapPoolsComponents.get(poolName), pkgDestiny)
                ).collect(Collectors.toList()));
        System.out.println(Roaster.format(entitas.toString()));

        return result;
    }


    private static void addAddMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "%2$s component = createComponent(%1$s.%2$s, %2$s.class);\n %3$s\n addComponent(%1$s.%2$s, component);\n ";
            source.addMethod()
                    .setName(String.format("add%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setParameters(memberNamesWithType(info.memberInfos))
                    .setBody(String.format(method,
                            CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName, memberAssignments(info.memberInfos)));


        }
    }

    private static void addReplaceMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "%2$s component = createComponent(%1$s.%2$s, %2$s.class);\n %3$s\n replaceComponent(%1$s.%2$s, component);\n ";
            source.addMethod()
                    .setName(String.format("replace%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setParameters(memberNamesWithType(info.memberInfos))
                    .setBody(String.format(method,
                            CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName, memberAssignments(info.memberInfos)));


        }
    }

    private static void addRemoveMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "removeComponent(%1$s.%2$s);";
            source.addMethod()
                    .setName(String.format("remove%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setBody(String.format(method,
                            CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));


        }
    }

    static String memberNamesWithType(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(info -> info.getType() + " " + "_" + info.getName())
                .collect(Collectors.joining(", "));

    }

    static String memberAssignments(List<FieldSource<JavaClassSource>> memberInfos) {
        String format = "component.%1$s = %2$s;";
        return memberInfos.stream().map(
                info -> {
                    String newArg = "_" + info.getName();
                    return String.format(format, info.getName(), newArg);
                }
        ).collect(Collectors.joining("\n"));

    }

    public static String memberNames(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(info -> "_" + info.getName())
                .collect(Collectors.joining(", "));
    }

    private JavaClassSource generateComponentExtension(ComponentInfo info, JavaClassSource entitas) {
        addEntityMethods(info, entitas);

        if (info.isSingleEntity) {
            addPoolMethods(info, entitas);
        }
        return entitas;
    }

    private JavaClassSource generateMatchers(String poolName, List<ComponentInfo> componentInfos, String pkgDestiny) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                CodeGenerator.capitalize(poolName) + "Matcher"));
        javaClass.setPackage(pkgDestiny);
        javaClass.addImport("com.ilargia.games.entitas.interfaces.IMatcher");
        javaClass.addImport("com.ilargia.games.entitas.matcher.Matcher");

        for (ComponentInfo info : componentInfos) {
            addMatcher(poolName, info, javaClass);
            addMatcherMethods(poolName, info, javaClass);
        }
        System.out.println(javaClass);
        return javaClass;
    }

    private void addPoolMethods(ComponentInfo info, JavaClassSource entitas) {
        addPoolGetMethods(info, entitas);
        addPoolHasMethods(info, entitas);
        addPoolAddMethods(info, entitas);
        addPoolReplaceMethods(info, entitas);
        addPoolRemoveMethods(info, entitas);
    }

    private void addPoolGetMethods(ComponentInfo info, JavaClassSource source) {
        source.addMethod()
                .setName(String.format("get%1$sEntity", CodeGenerator.capitalize(info.typeName)))
                .setReturnType("Entity")
                .setPublic()
                .setStatic(true)
                .setParameters("Pool pool")
                .setBody(String.format("return pool.getGroup(%1$sMatcher.%2$s).getSingleEntity();"
                        , CodeGenerator.capitalize(info.pools.get(0)), CodeGenerator.capitalize(info.typeName)));

        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("get%1$s", info.typeName))
                    .setReturnType(info.typeName)
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Pool pool")
                    .setBody(String.format("return Entitas.get%1$s(Entitas.get%1$sEntity(pool));"
                            , CodeGenerator.capitalize(info.typeName)));

        }
    }

    private void addPoolHasMethods(ComponentInfo info, JavaClassSource source) {
        if (info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("%1$s%2$s", info.singleComponentPrefix, CodeGenerator.capitalize(info.typeName)))
                    .setReturnType("boolean")
                    .setPublic()
                    .setStatic(true)
                    .setFinal(true)
                    .setParameters("Pool pool")
                    .setBody(String.format("return Entitas.get%1$sEntity(pool) != null;",
                            CodeGenerator.capitalize(info.typeName)));

            source.addMethod()
                    .setName(String.format("set%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setStatic(true)
                    .setFinal(true)
                    .setParameters("Pool pool, boolean value")
                    .setBody(String.format(" Entity entity = Entitas.get%1$sEntity(pool);\n" +
                            "        if(value != (entity != null)) {\n" +
                            "             if(value) {\n" +
                            "                  Entitas.set%1$s(pool.createEntity(), true);\n" +
                            "             } else {\n" +
                            "                  pool.destroyEntity(entity);\n" +
                            "             }\n" +
                            "        }", CodeGenerator.capitalize(info.typeName)));


        } else {
            source.addMethod()
                    .setName(String.format("has%1$s", CodeGenerator.capitalize(info.typeName)))
                    .setReturnType("boolean")
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Pool pool")
                    .setBody(String.format("return Entitas.get%1$sEntity(pool) != null; ",
                            CodeGenerator.capitalize(info.typeName)));

        }
    }

    private void addPoolAddMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("set%1$s", CodeGenerator.capitalize(info.typeName)))
                    .setReturnType("Entity")
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Pool pool," + memberNamesWithType(info.memberInfos))
                    .setBody(String.format("if(Entitas.has%1$s(pool)) {\n" +
                                    "            throw new EntitasException(\"Could not set %1$s!\" + pool + \" already has an entity with %1$s!\", " +
                                    "\"You should check if the pool already has a %1$sEntity before setting it or use pool.Replace%1$s().\");" +
                                    "         }\n" +
                                    "         Entity entity = pool.createEntity();\n" +
                                    "         Entitas.add%1$s(entity, %2$s);\n" +
                                    "         return entity;"
                            , CodeGenerator.capitalize(info.typeName), memberNames(info.memberInfos)));

        }
    }

    private void addPoolReplaceMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("replace%1$s", CodeGenerator.capitalize(info.typeName)))
                    .setReturnType("Entity")
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Pool pool," + memberNamesWithType(info.memberInfos))
                    .setBody(String.format("Entity entity = Entitas.get%1$sEntity(pool);" +
                                    "         if(entity == null) {" +
                                    "            entity = Entitas.set%1$s(pool, %2$s);" +
                                    "         } else { " +
                                    "           Entitas.replace%1$s(entity, %2$s);" +
                                    "         }" +
                                    "         return entity;"
                            , CodeGenerator.capitalize(info.typeName), memberNames(info.memberInfos)));

        }
    }

    private void addPoolRemoveMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("remove%1$s", CodeGenerator.capitalize(info.typeName)))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Pool pool")
                    .setBody(String.format("pool.destroyEntity(Entitas.get%1$sEntity(pool));"
                            , CodeGenerator.capitalize(info.typeName), memberNames(info.memberInfos)));

        }

    }

    private JavaClassSource addMatcher(String poolName, ComponentInfo info, JavaClassSource javaClass) {
        javaClass.addField()
                .setName("_matcher" + CodeGenerator.capitalize(info.typeName))
                .setType("IMatcher")
                .setPublic()
                .setStatic(true);
        return null;
    }

    private void addMatcherMethods(String poolName, ComponentInfo info, JavaClassSource javaClass) {
        String body = "if (_matcher%2$s == null) {" +
                "   Matcher matcher = (Matcher)Matcher.AllOf(%1$s.%2$s);" +
                "   matcher.componentNames = %1$s.componentNames();" +
                "   _matcher%2$s = matcher;" +
                "}" +
                "return _matcher%2$s;";

        javaClass.addMethod()
                .setName(CodeGenerator.capitalize(info.typeName))
                .setReturnType("IMatcher")
                .setPublic()
                .setStatic(true)
                .setBody(String.format(body, CodeGenerator.capitalize(poolName) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        CodeGenerator.capitalize(info.typeName)));

    }

    private void addEntityMethods(ComponentInfo componentInfo, JavaClassSource source) {
        addGetMethods(componentInfo, source);
        addHasMethods(componentInfo, source);
        addAddMethods(componentInfo, source);
        addReplaceMethods(componentInfo, source);
        addRemoveMethods(componentInfo, source);

    }

    private void addGetMethods(ComponentInfo info, JavaClassSource source) {
        source.addImport(info.fullTypeName);

        if (info.isSingletonComponent) {
            source.addField()
                    .setName(info.typeName.toLowerCase() + CodeGenerator.COMPONENT_SUFFIX)
                    .setType(info.typeName)
                    .setLiteralInitializer(String.format("new %1$s();", info.typeName))
                    .setPublic();

        } else {
            source.addMethod()
                    .setName(String.format("get%1$s", info.typeName))
                    .setReturnType(info.typeName)
                    .setPublic()
                    .setBody(String.format("return (%1$s)getComponent(%2$s.%1$s);"
                            , info.typeName, CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG));

        }
    }

    private void addHasMethods(ComponentInfo info, JavaClassSource source) {
        source.addImport(info.fullTypeName);

        if (info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("%1$s%2$s", info.singleComponentPrefix, info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));

            source.addMethod()
                    .setName(String.format("set%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setParameters("boolean value")
                    .setBody(String.format(" if(value != hasComponent(%1$s.%2$s)) {\n" +
                                    "                    if(value) {\n" +
                                    "                        addComponent(%1$s.%2$s, %3$s);\n" +
                                    "                    } else {\n" +
                                    "                        removeComponent(%1$s.%2$s);\n" +
                                    "                    }\n" +
                                    "                }", CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName, info.nameComponent));


        } else {
            source.addMethod()
                    .setName(String.format("has%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return hasComponent(%1$s.%2$s);",
                            CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));

        }
    }


}
