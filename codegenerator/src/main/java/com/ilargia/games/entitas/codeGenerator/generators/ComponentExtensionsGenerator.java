package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.Import;
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

        JavaClassSource entityClass = Roaster.parse(JavaClassSource.class, "public class Entity extends com.ilargia.games.entitas.Entity {}");
        entityClass.setPackage(pkgDestiny);
        entityClass.addMethod()
                .setName("Entity")
                .setPublic()
                .setConstructor(true)
                .setParameters("int totalComponents,Stack<IComponent>[] componentPools, PoolMetaData poolMetaData")
                .setBody("super(totalComponents, componentPools, poolMetaData);");
        entityClass.addImport("com.ilargia.games.entitas.PoolMetaData");
        entityClass.addImport("com.ilargia.games.entitas.interfaces.IComponent");
        entityClass.addImport("java.util.Stack");

        JavaClassSource poolClass = Roaster.parse(JavaClassSource.class, "public class Pool extends com.ilargia.games.entitas.BasePool<Entity> {}");
        poolClass.setPackage(pkgDestiny);

        poolClass.addMethod()
                .setName("Pool")
                .setPublic()
                .setConstructor(true)
                .setParameters("int totalComponents, int startCreationIndex, PoolMetaData metaData, FactoryEntity<Entity> factoryMethod")
                .setBody("super(totalComponents, startCreationIndex, metaData, factoryMethod);");
        poolClass.addImport("com.ilargia.games.entitas.PoolMetaData");
        poolClass.addImport("com.ilargia.games.entitas.interfaces.FactoryEntity");

        result.add(entityClass);
        result.add(poolClass);

        for (ComponentInfo info : infos) {
            if (info.generateMethods) {
                addEntityMethods(info, entityClass);
                if (info.isSingleEntity) {
                    addPoolMethods(info, poolClass);
                }
            }
        }

        result.addAll((List) mapPoolsComponents.keySet().stream()
                .map(poolName -> generateMatchers(poolName, mapPoolsComponents.get(poolName), pkgDestiny)
                ).collect(Collectors.toList()));
        System.out.println(Roaster.format(entityClass.toString()));

        return result;
    }


    private void addAddMethods(ComponentInfo info, JavaClassSource source) {
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

    private void addReplaceMethods(ComponentInfo info, JavaClassSource source) {
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

    private void addRemoveMethods(ComponentInfo info, JavaClassSource source) {
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

    private void addPoolMethods(ComponentInfo info, JavaClassSource poolClass) {

        addPoolGetMethods(info, poolClass);
        addPoolHasMethods(info, poolClass);
        addPoolAddMethods(info, poolClass);
        addPoolReplaceMethods(info, poolClass);
        addPoolRemoveMethods(info, poolClass);
    }

    private void addPoolGetMethods(ComponentInfo info, JavaClassSource source) {
        source.addMethod()
                .setName(String.format("get%1$sEntity", info.typeName))
                .setReturnType("Entity")
                .setPublic()
                .setBody(String.format("return getGroup(%1$sMatcher.%2$s()).getSingleEntity();"
                        , CodeGenerator.capitalize(info.pools.get(0)), info.typeName));

        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("get%1$s", info.typeName))
                    .setReturnType(info.typeName)
                    .setPublic()
                    .setBody(String.format("return get%1$sEntity().get%1$s();"
                            , info.typeName));

        }
    }

    private void addPoolHasMethods(ComponentInfo info, JavaClassSource source) {
        if (info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("is%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return get%1$sEntity() != null;",
                            info.typeName));

            source.addMethod()
                    .setName(String.format("set%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setParameters("boolean value")
                    .setBody(String.format(" Entity entity = get%1$sEntity();\n" +
                            "        if(value != (entity != null)) {\n" +
                            "             if(value) {\n" +
                            "                  entity.set%1$s(true);\n" +
                            "             } else {\n" +
                            "                  destroyEntity(entity);\n" +
                            "             }\n" +
                            "        }", info.typeName));


        } else {
            source.addMethod()
                    .setName(String.format("has%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return get%1$sEntity() != null; ",
                            info.typeName));

        }
    }

    private void addPoolAddMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("set%1$s", info.typeName))
                    .setReturnType("Entity")
                    .setPublic()
                    .setParameters(memberNamesWithType(info.memberInfos))
                    .setBody(String.format("if(has%1$s()) {\n" +
                                    "            throw new EntitasException(\"Could not set %1$s!\" + this + \" already has an entity with %1$s!\", " +
                                    "\"You should check if the pool already has a %1$sEntity before setting it or use pool.Replace%1$s().\");" +
                                    "         }\n" +
                                    "         Entity entity = createEntity();\n" +
                                    "         entity.add%1$s(%2$s);\n" +
                                    "         return entity;"
                            , info.typeName, memberNames(info.memberInfos)));
            if(source.getImport("EntitasException") == null) {
                source.addImport("com.ilargia.games.entitas.exceptions.EntitasException");
            }
            source.addImport(info.fullTypeName);

        }
    }

    private void addPoolReplaceMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("replace%1$s", info.typeName))
                    .setReturnType("Entity")
                    .setPublic()
                    .setParameters(memberNamesWithType(info.memberInfos))
                    .setBody(String.format("Entity entity = get%1$sEntity();" +
                                    "         if(entity == null) {" +
                                    "            entity = set%1$s(%2$s);" +
                                    "         } else { " +
                                    "           entity.replace%1$s(%2$s);" +
                                    "         }" +
                                    "         return entity;"
                            , info.typeName, memberNames(info.memberInfos)));


        }
    }

    private void addPoolRemoveMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("remove%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setBody(String.format("destroyEntity(get%1$sEntity());"
                            , info.typeName, memberNames(info.memberInfos)));

        }

    }

    private JavaClassSource addMatcher(String poolName, ComponentInfo info, JavaClassSource javaClass) {
        javaClass.addField()
                .setName("_matcher" + info.typeName)
                .setType("IMatcher")
                .setPrivate()
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
                .setName(info.typeName)
                .setReturnType("IMatcher")
                .setPublic()
                .setStatic(true)
                .setBody(String.format(body, CodeGenerator.capitalize(poolName) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                       info.typeName));

    }

    private void addEntityMethods(ComponentInfo componentInfo, JavaClassSource source) {
        addGetMethods(componentInfo, source);
        addHasMethods(componentInfo, source);
        addAddMethods(componentInfo, source);
        addReplaceMethods(componentInfo, source);
        addRemoveMethods(componentInfo, source);
        addImportClass(componentInfo.memberInfos, source);

    }

    private void addGetMethods(ComponentInfo info, JavaClassSource source) {
        source.addImport(info.fullTypeName);

        if (info.isSingletonComponent) {
            source.addField()
                    .setName(info.typeName + CodeGenerator.COMPONENT_SUFFIX)
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


    public String memberNamesWithType(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(info -> info.getType() + " " + "_" + info.getName())
                .collect(Collectors.joining(", "));

    }

    public void addImportClass(List<FieldSource<JavaClassSource>> memberInfos, JavaClassSource source) {
        for (FieldSource<JavaClassSource> info: memberInfos ){
            if(info.getOrigin().getImport(info.getType().toString()) != null) {
                if(source.getImport(info.getType().toString()) == null) {
                    source.addImport(info.getType());
                }
            }
        }

    }

    public String memberAssignments(List<FieldSource<JavaClassSource>> memberInfos) {
        String format = "component.%1$s = %2$s;";
        return memberInfos.stream().map(
                info -> {
                    String newArg = "_" + info.getName();
                    return String.format(format, info.getName(), newArg);
                }
        ).collect(Collectors.joining("\n"));

    }

    public String memberNames(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(info -> "_" + info.getName())
                .collect(Collectors.joining(", "));
    }

}
