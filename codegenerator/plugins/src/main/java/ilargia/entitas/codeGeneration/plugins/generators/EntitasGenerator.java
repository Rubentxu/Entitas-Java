package ilargia.entitas.codeGeneration.plugins.generators;

import ilargia.entitas.Context;
import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.config.TargetPackageConfig;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.apache.commons.lang3.text.WordUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.*;

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsComponentDataProvider.getContextNames;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods;


public class EntitasGenerator implements ICodeGenerator<JavaClassSource>, IConfigurable {
    public static final String DEFAULT_COMPONENT_LOOKUP_TAG = "ComponentsLookup";
    private TargetPackageConfig targetPackageConfig;
    private Map<String, String> contexts;

    public EntitasGenerator() {
        targetPackageConfig = new TargetPackageConfig();
        contexts = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Entitas contexts";
    }

    @Override
    public Integer gePriority() {
        return 0;
    }

    @Override
    public boolean isEnableByDefault() {
        return true;
    }

    @Override
    public boolean runInDryMode() {
        return true;
    }

    @Override
    public Properties defaultProperties() {
        return targetPackageConfig.defaultProperties();
    }

    @Override
    public void setProperties(Properties properties) {
        targetPackageConfig.setProperties(properties);
    }

    @Override
    public List<CodeGenFile<JavaClassSource>> generate(List<CodeGeneratorData> dataList) {
        dataList.stream()
                .filter(d -> d instanceof ComponentData)
                .map(d -> (ComponentData) d)
                .filter(d-> !d.containsKey("entityIndex_type"))
                .filter(d -> shouldGenerateMethods(d))
                .forEach(d -> generateContexts(d));

        String subDir = ((ComponentData)dataList.get(0)).getSubDir();
        String pkgDestiny = targetPackageConfig.getTargetPackage();
        if (!pkgDestiny.endsWith(subDir)) {
            pkgDestiny += "." + subDir;

        }

        String finalPkgDestiny = pkgDestiny;
        return new ArrayList<CodeGenFile<JavaClassSource>>(){{
            add(generateEntitas(contexts.keySet(), finalPkgDestiny));
        }};

    }

    private void generateContexts(ComponentData data) {
        getContextNames(data).stream()
                .forEach(contextName -> {
                    if (!contexts.containsKey(contextName)) {
                        String pkgDestiny = targetPackageConfig.getTargetPackage();
                        if (!pkgDestiny.endsWith(data.getSubDir())) {
                            pkgDestiny += "." + data.getSubDir();

                        }
                        contexts.put(contextName, pkgDestiny);
                    }
                });

    }


    private CodeGenFile<JavaClassSource> generateEntitas(Set<String> contextNames, String pkgDestiny) {
        JavaClassSource sourceGen = Roaster.parse(JavaClassSource.class, "public class Entitas implements IContexts{}");
        CodeGenFile<JavaClassSource> genFile = new CodeGenFile<JavaClassSource>( "Entitas", sourceGen, "");
        sourceGen.setPackage(pkgDestiny);
        createMethodConstructor(sourceGen, contextNames);
        createContextsMethod(sourceGen, contextNames);
        createMethodAllContexts(sourceGen, contextNames);
        createContextFields(sourceGen, contextNames);

        System.out.println(genFile.getFileContent());
        return genFile;

    }


    private void createContextsMethod(JavaClassSource codeGen, Set<String> contextNames) {
        contextNames.forEach((contextName) -> {
            String createMethodName = String.format("create%1$sContext", WordUtils.capitalize(contextName));
            String body = String.format("return new %1$sContext(%2$s.totalComponents, 0, new ContextInfo(\"%1$s\", %2$s.componentNames(), %2$s.componentTypes()), factory%1$sEntity());",
                    contextName, WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG);
            codeGen.addMethod()
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
                acc + "    " + contextName.toLowerCase() + " = create" + WordUtils.capitalize(contextName) + "Context();\n "
        );


        javaClass.addMethod()
                .setConstructor(true)
                .setPublic()
                .setBody(setAllContexts);
    }

    private void createContextFields(JavaClassSource codeGen, Set<String> contextNames) {
        codeGen.addImport(Context.class);
        codeGen.addImport("ilargia.entitas.api.*");
        codeGen.addImport("ilargia.entitas.api.entitas.EntityBaseFactory");

        contextNames.forEach((contextName) -> {
            if(contexts.containsKey(contextName)) {
                codeGen.addImport(contexts.get(contextName) + "." + contextName + "Context");
                codeGen.addImport(contexts.get(contextName) + "." + contextName + "ComponentsLookup");
                codeGen.addImport(contexts.get(contextName) + "." + contextName + "Entity");
            }
            codeGen.addMethod()
                    .setName(String.format("factory%1$sEntity", contextName))
                    .setReturnType(String.format("EntityBaseFactory<%1$sEntity>", contextName))
                    .setPublic()
                    .setBody(String.format("  return () -> { \n" +
                            "                   return new %1$sEntity();\n" +
                            "        };", contextName));
            codeGen.addField()
                    .setName(contextName.toLowerCase())
                    .setType(contextName + "Context")
                    .setPublic();
        });


    }

}
