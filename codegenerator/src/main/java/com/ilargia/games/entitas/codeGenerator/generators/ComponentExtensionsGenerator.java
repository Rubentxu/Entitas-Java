package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentExtensionsGenerator implements IComponentCodeGenerator {

    @Override
    public List<JavaClassSource> generate(ComponentInfo[] componentInfos, String pkgDestiny) {
        List<JavaClassSource> result = new ArrayList<>();
        JavaClassSource entitas = Roaster.parse(JavaClassSource.class, "public class Entitas {}");
        entitas.setPackage(pkgDestiny);

        JavaClassSource matcher = Roaster.parse(JavaClassSource.class, "public class Entitas {}");
        matcher.setPackage(pkgDestiny);

        for (int i = 0; i <componentInfos.length; i++) {
            if(componentInfos[i].generateMethods)
                generateComponentExtension(componentInfos[i], entitas, matcher);

        }

        System.out.println(Roaster.format(entitas.toString()));
        System.out.println(Roaster.format(matcher.toString()));
        result.add(entitas);
        result.add(matcher);
        return result;
    }

    private JavaClassSource generateComponentExtension(ComponentInfo info, JavaClassSource entitas, JavaClassSource matcher) {
        addEntityMethods(info, entitas);

        if(info.generateComponent) {
            // Add default matcher
            addMatcher(info, true);

            // Add custom matchers
//            code += addMatcher(componentInfo);
//            return addUsings("Entitas")
//                    + generateComponent(componentInfo)
//                    + code;
        }
        return entitas;
    }

    private void addMatcher(ComponentInfo info, boolean b) {

    }


    private void addEntityMethods(ComponentInfo componentInfo, JavaClassSource source ) {
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
                    .setName(info.typeName.toLowerCase())
                    .setType(info.typeName)
                    .setLiteralInitializer(String.format("new %1$s();", info.typeName))
                    .setPublic()
                    .setStatic(true)
                    .setFinal(true);

        } else {
            source.addMethod()
                    .setName(String.format("get%1$s", info.typeName))
                    .setReturnType(info.typeName)
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Entity entity")
                    .setBody(String.format("return (%1$s)entity.getComponent(%2$s.%1$s);"
                            , info.typeName, CodeGenerator.capitalize(info.pools[0]) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG));

        }
    }

    private void addHasMethods(ComponentInfo info, JavaClassSource source) {
        source.addImport(info.fullTypeName);

        if (info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("%1$s%2$s", info.singleComponentPrefix, info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setStatic(true)
                    .setFinal(true)
                    .setParameters("Entity entity")
                    .setBody(String.format("return entity.hasComponent(%1$s.%2$s);",
                            CodeGenerator.capitalize(info.pools[0]) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));

            source.addMethod()
                    .setName(String.format("set%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setStatic(true)
                    .setFinal(true)
                    .setParameters("Entity entity")
                    .setBody(String.format(" if(value != %3$s) {\n" +
                                    "                    if(value) {\n" +
                                    "                        entity.addComponent(%1$s.%2$s, %3$s);\n" +
                                    "                    } else {\n" +
                                    "                        entity.removeComponent(%1$s.%2$s);\n" +
                                    "                    }\n" +
                                    "                }", CodeGenerator.capitalize(info.pools[0]) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName, info.nameComponent));


        } else {
            source.addMethod()
                    .setName(String.format("has%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Entity entity")
                    .setBody(String.format("return entity.hasComponent(%1$s.%2$s);",
                            CodeGenerator.capitalize(info.pools[0]) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));

        }
    }

    private static void addAddMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "%2$s component = entity.createComponent(%1$s.%2$s, %2$s.class);\n %3$s\n entity.addComponent(%1$s.%2$s, component);\n ";
            source.addMethod()
                    .setName(String.format("add%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Entity entity, " + memberNamesWithType(info.memberInfos))
                    .setBody(String.format(method,
                            CodeGenerator.capitalize(info.pools[0]) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName, memberAssignments(info.memberInfos)));


        }
    }

    private static void addReplaceMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "%2$s component = entity.createComponent(%1$s.%2$s, %2$s.class);\n %3$s\n entity.replaceComponent(%1$s.%2$s, component);\n ";
            source.addMethod()
                    .setName(String.format("replace%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Entity entity, " + memberNamesWithType(info.memberInfos))
                    .setBody(String.format(method,
                            CodeGenerator.capitalize(info.pools[0]) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName, memberAssignments(info.memberInfos)));


        }
    }

    private static void addRemoveMethods(ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            String method = "entity.removeComponent(%1$s.%2$s);";
            source.addMethod()
                    .setName(String.format("remove%1$s", info.typeName))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Entity entity, ")
                    .setBody(String.format(method,
                            CodeGenerator.capitalize(info.pools[0]) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
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


}
