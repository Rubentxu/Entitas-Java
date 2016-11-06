package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IPoolCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
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
        return addClassHeader(lookupTag)
                + addIndices(componentInfos)
                + addComponentNames(componentInfos)
                + addComponentTypes(componentInfos)
                + addCloseClass();
    }

    public static String addClassHeader(String lookupTag) {
        return String.format("public static class {0} {{\n", lookupTag);
    }

    public static String addIndices(ComponentInfo[] componentInfos) {
        String fieldFormat = "    public static final int {0} = {1};\n";
        String totalFormat = "    public static final int TotalComponents = {0};";
        String code = "";
        for (int i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            if (info != null) {
                code += String.format(fieldFormat, info.typeName, i);
            }
        }

        if (code != "") {
            code = "\n" + code;
        }

        String totalComponents = String.format(totalFormat, componentInfos.length);
        return code + "\n" + totalComponents;
    }

    public static String addComponentNames(ComponentInfo[] componentInfos) {
        String format = "        \"{1}\",\n";
        String nullFormat = "        null,\n";
        String code = "";
        for (int i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            if (info != null) {
                code += String.format(format, i, info.typeName);
            } else {
                code += nullFormat;
            }
        }
        if (code.endsWith(",\n")) {
            code = code.replace("\n", "");
        }

        return String.format(String.join("\n",
                "public static String[] componentNames = {{ {0} }};", code));

    }

    public static String addComponentTypes(ComponentInfo[] componentInfos) {
        String format = "        typeof({1}),\n";
        String nullFormat = "        null,\n";
        String code = "";
        for (int i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            if (info != null) {
                code += String.format(format, i, info.fullTypeName);
            } else {
                code += nullFormat;
            }
        }
        if (code.endsWith(",\n")) {
            code = code.replace("\n", "");
        }

        return String.format(String.join("\n",
                "public static System.Type[] componentTypes = {{ {{ {0} }};", code));


    }

    static String addCloseClass() {
        return "\n}\n";
    }

}
