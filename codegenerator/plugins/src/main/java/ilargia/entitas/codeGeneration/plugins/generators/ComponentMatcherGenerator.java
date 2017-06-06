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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.getTypeName;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsDataProvider.getContextNames;
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods;
import static ilargia.entitas.codeGeneration.plugins.generators.ComponentEntityGenerator.DEFAULT_COMPONENT_LOOKUP_TAG;


public class ComponentMatcherGenerator implements ICodeGenerator<JavaClassSource>, IConfigurable {
    private TargetPackageConfig targetPackageConfig;
    private Map<String, CodeGenFile<JavaClassSource>> contexts;

    public ComponentMatcherGenerator() {
        targetPackageConfig = new TargetPackageConfig();
        contexts = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Component (Matcher API)";
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
    public List<CodeGenFile<JavaClassSource>> generate(List<CodeGeneratorData> data) {
        data.stream()
                .filter(d -> d instanceof ComponentData)
                .map(d -> (ComponentData) d)
                .filter(d -> shouldGenerateMethods(d))
                .forEach(d -> generateEntities(d));

        contexts.values().forEach(f -> System.out.println(f.getFileContent().toString()));
        return contexts.values().stream().collect(Collectors.toList());
    }

    private void generateEntities(ComponentData data) {
        getContextNames(data).stream()
                .forEach(contextName -> generateEntity(contextName, data));
    }

    private void generateEntity(String contextName, ComponentData data) {
        String pkgDestiny = targetPackageConfig.targetPackage();
        CodeGenFile<JavaClassSource> genFile = getCodeGenFile(contextName, data);
        JavaClassSource codeGenerated = genFile.getFileContent();

        if (!pkgDestiny.endsWith(data.getSubDir())) {
            pkgDestiny += "." + data.getSubDir();

        }

        codeGenerated.setPackage(pkgDestiny);
        codeGenerated.addImport("Matcher");

        addMatcher(contextName, data, codeGenerated);
        addMatcherMethods(contextName, data, codeGenerated);

    }

    private CodeGenFile<JavaClassSource> getCodeGenFile(String contextName, ComponentData data) {
        if (contexts.containsKey(contextName)) {
            return contexts.get(contextName);
        } else {
            JavaClassSource sourceGen = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                    WordUtils.capitalize(contextName) + "Matcher"));
            CodeGenFile<JavaClassSource> genFile = new CodeGenFile<JavaClassSource>(contextName + "Context.java", sourceGen, data.getSubDir());
            contexts.put(contextName, genFile);
            return genFile;
        }
    }


    private void addMatcher(String contextName, ComponentData data, JavaClassSource codeGenerated) {
        codeGenerated.addField()
                .setName("_matcher" + getTypeName(data))
                .setType("Matcher")
                .setPrivate()
                .setStatic(true);

    }

    private void addMatcherMethods(String contextName, ComponentData data, JavaClassSource codeGenerated) {
        String body = "if (_matcher%2$s == null) {" +
                "   Matcher matcher = (Matcher)Matcher.AllOf(%1$s.%2$s);" +
                "   matcher.componentNames = %1$s.componentNames();" +
                "   _matcher%2$s = matcher;" +
                "}" +
                "return _matcher%2$s;";

        codeGenerated.addMethod()
                .setName(getTypeName(data))
                .setReturnType("Matcher")
                .setPublic()
                .setStatic(true)
                .setBody(String.format(body, WordUtils.capitalize(contextName) + DEFAULT_COMPONENT_LOOKUP_TAG,
                        getTypeName(data)));

    }

}
