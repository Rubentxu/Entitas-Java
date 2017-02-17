package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EntitasGenerator implements IComponentCodeGenerator {


    @Override
    public List<JavaClassSource> generate(List<ComponentInfo> infos, String pkgDestiny) {
        Map<String, List<ComponentInfo>> mapContextsComponents = CodeGenerator.generateMap(infos);

        List<JavaClassSource> result = new ArrayList<>();
        ;

        result.addAll((List) mapContextsComponents.keySet().stream()
                .map(contextName -> generateEntitas(mapContextsComponents.keySet(), pkgDestiny)
                ).collect(Collectors.toList()));

        return result;
    }


    public JavaClassSource generateEntitas(Set<String> contextNames, String pkgDestiny) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, "public class Entitas implements IContexts{}");
        javaClass.setPackage(pkgDestiny);
        createMethodConstructor(javaClass, contextNames);
        createContextsMethod(javaClass, contextNames);
        createMethodAllContexts(javaClass, contextNames);
        createContextFields(javaClass, contextNames);

        return javaClass;

    }


    private void createContextsMethod(JavaClassSource javaClass, Set<String> contextNames) {
        contextNames.forEach((contextName) -> {
            String createMethodName = String.format("create%1$sContext", CodeGenerator.capitalize(contextName));
            String body = String.format("return new %1$sContext(%2$s.totalComponents, 0, new ContextInfo(\"%1$s\", %2$s.componentNames(), %2$s.componentTypes()), factory%1$sEntity());",
                    contextName, CodeGenerator.capitalize(contextName) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG);
            javaClass.addMethod()
                    .setPublic()
                    .setName(createMethodName)
                    .setReturnType(contextName + "Context")
                    .setBody(body);

        });

    }

    private void createMethodAllContexts(JavaClassSource javaClass, Set<String> contextNames) {

        String allContextsList = contextNames.stream().reduce("", (acc, contextName) -> {
            return acc + contextName.toLowerCase() + ", ";
        });

        javaClass.addMethod()
                .setPublic()
                .setName("allContexts")
                .setReturnType("Context[]")
                .setBody(String.format("return new Context[] { %1$s };", allContextsList));


    }

    private void createMethodConstructor(JavaClassSource javaClass, Set<String> contextNames) {
        String setAllContexts = contextNames.stream().reduce("\n", (acc, contextName) ->
                acc + "    " + contextName.toLowerCase() + " = create" + CodeGenerator.capitalize(contextName) + "Context();\n "
        );


        javaClass.addMethod()
                .setConstructor(true)
                .setPublic()
                .setBody(setAllContexts);
    }

    private void createContextFields(JavaClassSource javaClass, Set<String> contextNames) {
        javaClass.addImport("java.util.Stack");
        javaClass.addImport("com.ilargia.games.entitas.Context");
        javaClass.addImport("com.ilargia.games.entitas.api.*");
        contextNames.forEach((contextName) -> {

            javaClass.addMethod()
                    .setName(String.format("factory%1$sEntity", contextName))
                    .setReturnType(String.format("EntityBaseFactory<%1$sEntity>", contextName))
                    .setPublic()
                    .setBody(String.format("  return () -> { \n" +
                            "                   return new %1$sEntity();\n" +
                            "        };", contextName));
            javaClass.addField()
                    .setName(contextName.toLowerCase())
                    .setType(contextName + "Context")
                    .setPublic();
        });


    }

}
