package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.Pool;
import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IPoolCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.Arrays;

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
        return new CodeGenFile[0];
    }

    private JavaClassSource generateIndicesLookup(String lookupTag, ComponentInfo[] componentInfos) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public static class {0} {}", lookupTag));
        addIndices(componentInfos, javaClass);
        addComponentNames(componentInfos, javaClass);
        addComponentTypes(componentInfos, javaClass);
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
                        .setStatic(true)
                        .setFinal(true)
                        .setPublic();
            }
        }
        javaClass.addField()
                .setName("totalComponents")
                .setType("int")
                .setLiteralInitializer(Integer.toString(componentInfos.length))
                .setStatic(true)
                .setFinal(true)
                .setPublic();


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
                .setStatic(true)
                .setPublic()
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
                .setStatic(true)
                .setPublic()
                .setBody(code);

    }


    private String capitalize(final String string) {
        char[] chars = string.toLowerCase().toCharArray();
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
