package com.ilargia.games.entitas.generators;


import com.ilargia.games.entitas.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.intermediate.ComponentInfo;
import com.ilargia.games.entitas.CodeGenerator;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

import java.util.ArrayList;
import java.util.Collections;
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
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                CodeGenerator.capitalize(poolName) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG));

//        if(componentInfos.size() > 0 && componentInfos.get(0).subDir !=null) {
//            pkgDestiny+= "."+componentInfos.get(0).subDir;
//
//        }
        if(componentInfos.size() > 0 && !pkgDestiny.endsWith(componentInfos.get(0).subDir)) {
            pkgDestiny+= "."+componentInfos.get(0).subDir;

        }
        javaClass.setPackage(pkgDestiny);

        addIndices(componentInfos, javaClass);
        addComponentNames(componentInfos, javaClass);
        addComponentTypes(componentInfos, javaClass);
        System.out.println(javaClass);
        return javaClass;
    }


    public JavaClassSource addIndices(List<ComponentInfo> componentInfos, JavaClassSource javaClass) {
        if (componentInfos.get(0).index != null) componentInfos.sort((ComponentInfo o, ComponentInfo o2) -> {
            return o.index.compareTo(o2.index);
        });

        for (ComponentInfo info : componentInfos) {
            if (info != null) {
                javaClass.addField()
                        .setName(info.typeName)
                        .setType("int")
                        .setLiteralInitializer(info.index.toString())
                        .setPublic()
                        .setStatic(true)
                        .setFinal(true);
            }
        }
        javaClass.addField()
                .setName("totalComponents")
                .setType("int")
                .setLiteralInitializer(Integer.toString(componentInfos.size()))
                .setPublic()
                .setStatic(true)
                .setFinal(true);


        return javaClass;

    }

    public void addComponentNames(List<ComponentInfo> componentInfos, JavaClassSource javaClass) {
        String format = " \"%1$s\",\n";
        String code = " return new String[] {";

        ArrayList<ComponentInfo> totalInfos = new ArrayList<>(Collections.nCopies(componentInfos.get(0).totalComponents, null));
        for (ComponentInfo info : componentInfos) {
            totalInfos.add(info.index, info);
        }
        totalInfos.subList(componentInfos.get(0).totalComponents, totalInfos.size()).clear();
        for (int i = 0; i < totalInfos.size(); i++) {
            ComponentInfo info = totalInfos.get(i);
            if (info != null && info.index == i) {
                code += String.format(format, info.typeName);
            } else {
                code += " null,\n";
            }

        }


        StringBuilder codeFinal = new StringBuilder(code);
        if (code.endsWith(",\n")) {
            codeFinal.replace(code.lastIndexOf(",\n"), code.lastIndexOf(",") + 1, " }; ");
        }
        javaClass.addMethod().setName("componentNames")
                .setReturnType("String[]")
                .setPublic()
                .setStatic(true)
                .setBody(codeFinal.toString());

    }

    public void addComponentTypes(List<ComponentInfo> componentInfos, JavaClassSource javaClass) {
        String format = " %1$s%2$s,\n";
        String code = "return new Class[] {";
        ArrayList<ComponentInfo> totalInfos = new ArrayList<>(Collections.nCopies(componentInfos.get(0).totalComponents, null));
        for (ComponentInfo info : componentInfos) {
            totalInfos.add(info.index, info);
        }
        totalInfos.subList(componentInfos.get(0).totalComponents, totalInfos.size()).clear();
        for (int i = 0; i < totalInfos.size(); i++) {
            ComponentInfo info = totalInfos.get(i);
            if (info != null && info.index == i) {
                code += String.format(format, info.typeName, ".class");
                javaClass.addImport(info.fullTypeName);
            } else {
                code += String.format(format, null, "");
            }

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

    public void addComponentFactories(ComponentInfo[] componentInfos, JavaClassSource javaClass) {
        String format = " %1$s.class,\n";
        String code = "return new FactoryComponent[] {";
        for (int i = 0; i < componentInfos.length; i++) {
            ComponentInfo info = componentInfos[i];
            JavaInterfaceSource interfaceSource = Roaster.parse(JavaInterfaceSource.class, String.format("public interface Factory%1$s extends FactoryComponent {}",
                    info.typeName));
            interfaceSource.addMethod()
                    .setName(String.format("create%1$s", info.typeName))
                    .setReturnType(info.typeName)
                    .setPublic();

            javaClass.addNestedType(interfaceSource.toString());


        }


    }

}
