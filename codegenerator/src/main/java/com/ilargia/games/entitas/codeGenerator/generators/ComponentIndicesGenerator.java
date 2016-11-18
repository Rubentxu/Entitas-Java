package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComponentIndicesGenerator implements IComponentCodeGenerator {

    @Override
    public List<JavaClassSource> generate(List<ComponentInfo> componentInfos, String pkgDestiny) {
        Map<String, List<ComponentInfo>> mapPoolsComponents = CodeGenerator.generateMap(componentInfos);

        return (List) mapPoolsComponents.keySet().stream()
                .map(poolName -> generateIndicesLookup(poolName, mapPoolsComponents.get(poolName), pkgDestiny)
                ).collect(Collectors.toList());

    }

    private JavaClassSource generateIndicesLookup(String poolName, List<ComponentInfo> componentInfos, String pkgDestiny) {
        return generateIndicesLookup(poolName, (ComponentInfo[]) componentInfos.toArray(new ComponentInfo[0]), pkgDestiny);
    }

    private JavaClassSource generateIndicesLookup(String poolName, ComponentInfo[] componentInfos, String pkgDestiny) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                CodeGenerator.capitalize(poolName) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG));
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
                        .setName(CodeGenerator.capitalize(info.typeName))
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
            codeFinal.replace(code.lastIndexOf(",\n"), code.lastIndexOf(",") + 1, " }; ");
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
            codeFinal.replace(code.lastIndexOf(",\n"), code.lastIndexOf(",") + 1, " }; ");
        }

        javaClass.addMethod()
                .setName("componentTypes")
                .setReturnType("Class[]")
                .setPublic()
                .setStatic(true)
                .setBody(codeFinal.toString());

    }

}
