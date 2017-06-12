package ilargia.entitas.codeGeneration.plugins.generators;

import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.config.TargetPackageConfig;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.apache.commons.lang3.text.WordUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsComponentDataProvider.getContextNames;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods;


public class EntitasGenerator implements ICodeGenerator<JavaClassSource>, IConfigurable {
    private static final String DEFAULT_COMPONENT_LOOKUP_TAG = "ComponentsLookup";
    private TargetPackageConfig targetPackageConfig;
    private Set<String> contexts;

    public EntitasGenerator() {
        targetPackageConfig = new TargetPackageConfig();
        contexts = new HashSet<>();
    }

    @Override
    public String getName() {
        return "Component (Context API)";
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
    public Properties getDefaultProperties() {
        return targetPackageConfig.getDefaultProperties();
    }

    @Override
    public void configure(Properties properties) {
        targetPackageConfig.configure(properties);
    }

    @Override
    public List<CodeGenFile<JavaClassSource>> generate(List<CodeGeneratorData> dataList) {
        dataList.stream()
                .filter(d -> d instanceof ComponentData)
                .map(d -> (ComponentData) d)
                .filter(d -> shouldGenerateMethods(d))
                .forEach(d -> generateContexts(d));

         subDir = ((CodeGeneratorData)dataList.get(0)).getSubDir();
        String pkgDestiny = targetPackageConfig.targetPackage();
        if (!pkgDestiny.endsWith(subDir.getSubDir())) {
            pkgDestiny += "." + dataList.getSubDir();

        }


        return contexts.stream().collect(Collectors.toList());
    }

    private void generateContexts(ComponentData data) {
        getContextNames(data).stream()
                .forEach(contextName -> {
                    if (!contexts.contains(contextName)) contexts.add(contextName);
                });
    }

    private void generateContext(String contextName, ComponentData data) {
        String pkgDestiny = targetPackageConfig.targetPackage();

        if (!contexts.contains(contextName)) {
            JavaClassSource sourceGen = Roaster.parse(JavaClassSource.class, String.format("public class %1$sContext extends Context<%1$sEntity> {}", contextName));
            CodeGenFile<JavaClassSource> genFile = new CodeGenFile<JavaClassSource>(contextName + "Context", sourceGen, data.getSubDir());
            contexts.put(contextName, genFile);
            JavaClassSource codeGenerated = genFile.getFileContent();



        }
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
            String createMethodName = String.format("create%1$sContext", WordUtils.capitalize(contextName));
            String body = String.format("return new %1$sContext(%2$s.totalComponents, 0, new ContextInfo(\"%1$s\", %2$s.componentNames(), %2$s.componentTypes()), factory%1$sEntity());",
                    contextName, WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG);
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
                acc + "    " + contextName.toLowerCase() + " = create" + WordUtils.capitalize(contextName) + "Context();\n "
        );


        javaClass.addMethod()
                .setConstructor(true)
                .setPublic()
                .setBody(setAllContexts);
    }

    private void createContextFields(JavaClassSource javaClass, Set<String> contextNames) {
        javaClass.addImport("Context");
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
