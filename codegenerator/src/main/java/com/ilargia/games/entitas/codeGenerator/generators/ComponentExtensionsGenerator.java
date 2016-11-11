package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentExtensionsGenerator implements IComponentCodeGenerator {

    @Override
    public CodeGenFile[] generate(ComponentInfo[] componentInfos, String pkgDestiny) {
        String generatorName = this.getClass().getName();

        return (CodeGenFile[]) ((List) Arrays.stream(componentInfos)
                .filter(info -> info.generateMethods)
                .map(info -> new CodeGenFile(info.fullTypeName + "GeneratedExtension",
                        generateComponentExtension(info),
                        generatorName))
                .collect(Collectors.toList()))
                .toArray(new CodeGenFile[0]);


    }

    private JavaClassSource generateComponentExtension(ComponentInfo info, String pkgDestiny) {

        code += addEntityMethods(componentInfo);
        return null;
    }


    private JavaClassSource addEntityMethods(ComponentInfo componentInfo, String pkgDestiny) {
        JavaClassSource source = Roaster.parse(JavaClassSource.class, "public class Entitas {}");
        source.setPackage(pkgDestiny);

        System.out.println(source);

        addGetMethods(componentInfo, source);
        addHasMethods(componentInfo, source);
        addAddMethods(componentInfo, source);
        addReplaceMethods(componentInfo, source);
        addRemoveMethods(componentInfo, source);
        addCloseClass();

        return source;
    }

    private void addGetMethods(ComponentInfo info, JavaClassSource source) {
        source.addImport(info.fullTypeName);

        if (info.isSingletonComponent) {
            source.addField()
                    .setName(capitalize(info.typeName))
                    .setType(info.typeName)
                    .setLiteralInitializer(String.format("new %s();", info.typeName))
                    .setPublic()
                    .setStatic(true)
                    .setFinal(true);

        } else {
            source.addMethod()
                    .setName(String.format("get%1$s()"))
                    .setReturnType(info.typeName)
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Entity entity")
                    .setBody(String.format("return (%1$s)entity.getComponent(%2$sComponentIds.%1$s);"
                            ,info.typeName, info.pools[0]));

        }
    }

    private void addHasMethods(ComponentInfo info, JavaClassSource source) {
        source.addImport(info.fullTypeName);

        if (info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("%1$s%2$s", info.singleComponentPrefix, capitalize(info.typeName)))
                    .setReturnType("boolean")
                    .setPublic()
                    .setStatic(true)
                    .setFinal(true)
                    .setBody(String.format("return entity.hasComponent(%1$s.%2$s)",
                            info.pools[0] + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName));

            source.addMethod()
                    .setName(String.format("set%1$s%2$s", info.singleComponentPrefix, capitalize(info.typeName)))
                    .setReturnTypeVoid()
                    .setPublic()
                    .setStatic(true)
                    .setFinal(true)
                    .setParameters("")
                    .setBody(String.format(" if(value != %3$s) {\n" +
                            "                    if(value) {\n" +
                            "                        addComponent(%1$s.%2$s, %3$s);\n" +
                            "                    } else {\n" +
                            "                        removeComponent(%1$s.%2$s);\n" +
                            "                    }\n" +
                            "                }", info.pools[0] + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG,
                            info.typeName, info.singleComponentPrefix + capitalize(info.typeName)));

            source.addMethod()
                    .setName(String.format("set%1$s%2$s", info.singleComponentPrefix, capitalize(info.typeName)))
                    .setReturnType("Entitas")
                    .setPublic()
                    .setStatic(true)
                    .setFinal(true)
                    .setParameters("bool value")
                    .setBody("%1$s");

        } else {
            source.addMethod()
                    .setName(String.format("get%1$s()"))
                    .setReturnType(info.typeName)
                    .setPublic()
                    .setStatic(true)
                    .setParameters("Entity entity")
                    .setBody(String.format("return (%1$s)entity.getComponent(%2$sComponentIds.%1$s);"
                            ,info.typeName, info.pools[0]));

        }
    }

    private static void addCloseClass() {

    }


    private static void addReplaceMethods(ComponentInfo componentInfo, JavaClassSource source) {

    }

    private static void addAddMethods(ComponentInfo componentInfo, JavaClassSource source) {

    }




    private static void addRemoveMethods(ComponentInfo componentInfo, JavaClassSource source) {

    }

    private String capitalize(final String String) {
        char[] chars = String.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

}
