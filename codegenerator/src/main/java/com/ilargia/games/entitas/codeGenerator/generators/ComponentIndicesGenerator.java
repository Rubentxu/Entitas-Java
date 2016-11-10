package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IPoolCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.*;
import java.util.stream.Collectors;

public class ComponentIndicesGenerator implements IComponentCodeGenerator {

    @Override
    public CodeGenFile[] generate(ComponentInfo[] componentInfos, String pkgDestiny) {
        Map<String, List<ComponentInfo>> mapPoolsComponents = generateMap(componentInfos);

        return (CodeGenFile[]) ((List) mapPoolsComponents.keySet().stream()
                .map(poolName -> {
                    return new CodeGenFile(
                            poolName,
                            generateIndicesLookup(poolName, mapPoolsComponents.get(poolName), pkgDestiny),
                            "ComponentIndicesGenerator");
                }).collect(Collectors.toList()))
                .toArray(new CodeGenFile[0]);

    }

    public Map<String, List<ComponentInfo>> generateMap(ComponentInfo[] componentInfos) {
        Map<String, List<ComponentInfo>> poolsComponents = new HashMap<>();

        for (int i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            for (int j = 0; j < info.pools.length; j++) {
                String poolName = info.pools[j];
                if (!poolsComponents.containsKey(poolName)) {
                    poolsComponents.put(poolName, new ArrayList<>());
                }
                List<ComponentInfo> list = poolsComponents.get(poolName);
                list.add(info);
            }
        }

        return poolsComponents;

    }

    private JavaClassSource generateIndicesLookup(String poolName, List<ComponentInfo> componentInfos, String pkgDestiny) {
        return  generateIndicesLookup(poolName, (ComponentInfo[]) componentInfos.toArray(new ComponentInfo[0]), pkgDestiny);
    }

    private JavaClassSource generateIndicesLookup(String poolName, ComponentInfo[] componentInfos, String pkgDestiny) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                capitalize(poolName) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG));
        javaClass.setPackage(pkgDestiny);
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
        String format = " \"%1$s\",\n";
        String code = " return new String[] {";
        for (int i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            if (info != null) {
                code += String.format(format, info.typeName);
            }
        }
        StringBuilder codeFinal = new StringBuilder(code);
        if (code.endsWith(",\n")) {
            codeFinal.replace(code.lastIndexOf(",\n"), code.lastIndexOf(",") + 1, " }; " );
        }
        javaClass.addMethod()
                .setName("componentNames")
                .setReturnType("String[]")
                .setPublic()
                .setStatic(true)
                .setBody(codeFinal.toString());

    }

    public void addComponentTypes(ComponentInfo[] componentInfos, JavaClassSource javaClass) {
        String format = " %1$s.class,\n";
        String code = "return new Class[] {";
        for (int i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            if (info != null) {
                code += String.format(format, info.typeName);
            }
            javaClass.addImport(info.fullTypeName);
        }
        StringBuilder codeFinal = new StringBuilder(code);
        if (code.endsWith(",\n")) {
            codeFinal.replace(code.lastIndexOf(",\n"), code.lastIndexOf(",") + 1, " }; " );
        }

        javaClass.addMethod()
                .setName("componentTypes")
                .setReturnType("Class[]")
                .setPublic()
                .setStatic(true)
                .setBody(codeFinal.toString());

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
