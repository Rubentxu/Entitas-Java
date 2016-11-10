package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IPoolCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentIndicesGenerator implements IComponentCodeGenerator, IPoolCodeGenerator {

    @Override
    public CodeGenFile[] generate(String[] poolNames) {
        ComponentInfo[] emptyInfos = new ComponentInfo[0];
        return (CodeGenFile[]) Arrays.stream(poolNames)
                .map((poolName) -> poolName + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG)
                .map(lookupTag -> {
                    return new CodeGenFile(
                            lookupTag,
                            generateIndicesLookup(lookupTag, emptyInfos),
                            "ComponentIndicesGenerator");
                }).toArray();

    }

    @Override
    public CodeGenFile[] generate(ComponentInfo[] componentInfos) {
        ComponentInfo[] orderedComponentInfos = (ComponentInfo[]) Arrays.stream(componentInfos)
                .sorted(Comparator.comparing((info) -> info.typeName))
                .toArray();

        return (CodeGenFile[]) Arrays.stream(componentInfos)
                .map(info -> {
                    return new CodeGenFile(
                            info.fullTypeName,
                            generateIndicesLookup(info.fullTypeName, componentInfos),
                            "ComponentIndicesGenerator");
                }).toArray();

    }

    private JavaClassSource generateIndicesLookup(String lookupTag, ComponentInfo[] componentInfos) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}", lookupTag));
        addIndices(componentInfos, javaClass);
        addComponentNames(componentInfos, javaClass);
        addComponentTypes(componentInfos, javaClass);
        System.out.println(javaClass);
        return javaClass;

    }


    public JavaClassSource addIndices(ComponentInfo[] componentInfos, JavaClassSource javaClass) {
        for (Integer i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            if (info != null) {
                javaClass.addField()
                        .setName(capitalize(info.typeName))
                        .setType("int")
                        .setLiteralInitializer(i.toString())
                        .setPublic()
                        .setStatic(true)
                        .setFinal(true);
            }
        }
        javaClass.addField()
                .setName("totalComponents")
                .setType("int")
                .setLiteralInitializer(Integer.toString(componentInfos.length))
                .setPublic()
                .setStatic(true)
                .setFinal(true);


        return javaClass;

    }

    public void addComponentNames(ComponentInfo[] componentInfos, JavaClassSource javaClass) {
        String format = "        \"{1}\",\n";
        String code = " return new String[] {";
        for (int i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            if (info != null) {
                code += String.format(format, i, info.typeName);
            }
        }
        if (code.endsWith(",\n")) {
            code = code.replace(",\n", " }");
        }
        javaClass.addMethod()
                .setName("componentNames")
                .setReturnType("String[]")
                .setPublic()
                .setStatic(true)
                .setBody(code);

    }

    public void addComponentTypes(ComponentInfo[] componentInfos, JavaClassSource javaClass) {
        String format = "        typeof({1}),\n";
        String code = "return new Class[] {";
        for (int i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            if (info != null) {
                code += String.format(format, i, info.fullTypeName);
            }
        }
        if (code.endsWith(",\n")) {
            code = code.replace("\n", " }");
        }

        javaClass.addMethod()
                .setName("componentTypes")
                .setReturnType("Class[]")
                .setPublic()
                .setStatic(true)
                .setBody(code);

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
