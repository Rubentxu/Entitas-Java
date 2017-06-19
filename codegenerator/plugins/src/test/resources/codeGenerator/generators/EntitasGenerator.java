package ilargia.entitas.codeGenerator.generators;


import ilargia.entitas.codeGenerator.CodeGeneratorOld;
import ilargia.entitas.codeGenerator.data.ComponentInfo;
import ilargia.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntitasGenerator implements IComponentCodeGenerator {


    @Override
    public List<JavaClassSource> generate(List<ComponentInfo> infos, String pkgDestiny) {
        Map<String, List<ComponentInfo>> mapContextsComponents = CodeGeneratorOld.generateMap(infos);

        List<JavaClassSource> result = new ArrayList<>();
        JavaClassSource source = generateEntitas(mapContextsComponents.keySet(), pkgDestiny);
        mapContextsComponents.forEach((context, lista) -> {
            String fullTypename = lista.get(0).fullTypeName;
        });
        result.add(source);

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
            String createMethodName = String.format("create%1$sContext", CodeGeneratorOld.capitalize(contextName));
            String body = String.format("return new %1$sContext(%2$s.totalComponents, 0, new ContextInfo(\"%1$s\", %2$s.componentNames(), %2$s.componentTypes()), factory%1$sEntity());",
                    contextName, CodeGeneratorOld.capitalize(contextName) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG);
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
                .setBody(String.format("return new Context[] { %1$s };", allContextsList))
                .addAnnotation("Override");


    }

    private void createMethodConstructor(JavaClassSource javaClass, Set<String> contextNames) {
        String setAllContexts = contextNames.stream().reduce("\n", (acc, contextName) ->
                acc + "    " + contextName.toLowerCase() + " = create" + CodeGeneratorOld.capitalize(contextName) + "Context();\n "
        );


        javaClass.addMethod()
                .setConstructor(true)
                .setPublic()
                .setBody(setAllContexts);
    }

    private void createContextFields(JavaClassSource javaClass, Set<String> contextNames) {
        javaClass.addImport("Context");
        javaClass.addImport("ilargia.entitas.api.*");
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
