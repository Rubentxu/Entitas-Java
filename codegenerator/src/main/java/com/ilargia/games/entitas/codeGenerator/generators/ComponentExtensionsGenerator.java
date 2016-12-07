package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster._shade.org.eclipse.jdt.core.dom.MethodDeclaration;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

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
                .setParameters("int totalComponents,Stack<IComponent>[] componentPools, EntityMetaData entityMetaData")
                .setBody("super(totalComponents, componentPools, entityMetaData);");
        entityClass.addImport("com.ilargia.games.entitas.EntityMetaData");
        entityClass.addImport("com.ilargia.games.entitas.interfaces.IComponent");
        entityClass.addImport("java.util.Stack");

        JavaClassSource poolClass = Roaster.parse(JavaClassSource.class, "public class Pool extends com.ilargia.games.entitas.BasePool<Entity> {}");
        poolClass.setPackage(pkgDestiny);

        poolClass.addMethod()
                .setName("Pool")
                .setPublic()
                .setConstructor(true)
                .setParameters("int totalComponents, int startCreationIndex, EntityMetaData metaData, FactoryEntity<Entity> factoryMethod")
                .setBody("super(totalComponents, startCreationIndex, metaData, factoryMethod);");
        poolClass.addImport("com.ilargia.games.entitas.EntityMetaData");
        poolClass.addImport("com.ilargia.games.entitas.interfaces.FactoryEntity");

        result.add(entityClass);
        result.add(poolClass);

        for (ComponentInfo info : infos) {
            if (info.generateMethods) {
                addImporEnums(info, entityClass);
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

    private void addImporEnums(ComponentInfo info, JavaClassSource entityClass) {
        if(info.internalEnums != null)
            info.internalEnums.stream().forEach(e -> entityClass.addImport(e));

    }


    private void addAddMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "";
            if (info.constructores !=null && info.constructores.size() > 0) {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} addComponent(%1$s.%2$s, component);\n return this;",
                        CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        info.typeName, bodyFromConstructor(info.constructores.get(0)), memberNamesFromConstructor(info.constructores.get(0)));

            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n } \n%3$s\n addComponent(%1$s.%2$s, component);\n return this;",
                        CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        info.typeName, memberAssignments(info.memberInfos));
            }

            source.addMethod()
                    .setName(String.format("add%1$s", info.typeName))
                    .setReturnType("Entity")
                    .setPublic()
                    .setParameters(info.constructores !=null && info.constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(info.constructores.get(0))
                            : memberNamesWithType(info.memberInfos))
                    .setBody(method);

        }
    }

    private void addReplaceMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method;
            if (info.constructores !=null && info.constructores.size() > 0) {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s(%4$s);\n } else {\n%3$s\n} replaceComponent(%1$s.%2$s, component);\n return this;"
                        , CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        info.typeName, bodyFromConstructor(info.constructores.get(0)), memberNamesFromConstructor(info.constructores.get(0)));
            } else {
                method = String.format("%2$s component = (%2$s) recoverComponent(%1$s.%2$s);\n if(component == null) { " +
                                "component = new %2$s();\n} %3$s\n removeComponent(%1$s.%2$s);\n return this;",
                        CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                        info.typeName, memberAssignments(info.memberInfos));
            }

            source.addMethod()
                    .setName(String.format("replace%1$s", info.typeName))
                    .setReturnType("Entity")
                    .setPublic()
                    .setParameters(info.constructores !=null && info.constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(info.constructores.get(0))
                            : memberNamesWithType(info.memberInfos))
                    .setBody(method);


        }
    }

    private void addRemoveMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "removeComponent(%1$s.%2$s);return this;";
            source.addMethod()
                    .setName(String.format("remove%1$s", info.typeName))
                    .setReturnType("Entity")
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
                    .setReturnType("Pool")
                    .setPublic()
                    .setParameters("boolean value")
                    .setBody(String.format(" Entity entity = get%1$sEntity();\n" +
                            "        if(value != (entity != null)) {\n" +
                            "             if(value) {\n" +
                            "                  entity.set%1$s(true);\n" +
                            "             } else {\n" +
                            "                  destroyEntity(entity);\n" +
                            "             }\n" +
                            "        }\n return this;", info.typeName));


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
            if (source.getImport("EntitasException") == null) {
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
                    .setReturnType("Pool")
                    .setPublic()
                    .setBody(String.format("destroyEntity(get%1$sEntity()); return this;"
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
                    .setReturnType("Entity")
                    .setPublic()
                    .setParameters("boolean value")
                    .setBody(String.format(" if(value != hasComponent(%1$s.%2$s)) {\n" +
                                    "                    if(value) {\n" +
                                    "                        addComponent(%1$s.%2$s, %3$s);\n" +
                                    "                    } else {\n" +
                                    "                        removeComponent(%1$s.%2$s);\n" +
                                    "                    }\n" +
                                    "                }\n return this;", CodeGenerator.capitalize(info.pools.get(0)) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
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


    public void addImportClass(List<FieldSource<JavaClassSource>> memberInfos, JavaClassSource source) {
        for (FieldSource<JavaClassSource> info : memberInfos) {
            if (info.getOrigin().getImport(info.getType().toString()) != null) {
                if (source.getImport(info.getType().toString()) == null) {
                    source.addImport(info.getType());
                }
            }
        }

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
                .map(info -> info.getType() + " " + info.getName())
                .collect(Collectors.joining(", "));
    }

    public String bodyFromConstructor(MethodSource<JavaClassSource> constructor) {
        return ((MethodDeclaration) constructor.getInternal()).getBody().statements()
                .stream()
                .map(line -> line.toString().replaceAll("this", "component").toString())
                .reduce((t, u) -> {
                    if (!u.toString().startsWith("component")) {
                        u = "component." + u;
                    }
                    return t + ";\n " + u;
                }).get().toString();
        //return constructor.getBody().replaceAll("this","component");
    }

}
