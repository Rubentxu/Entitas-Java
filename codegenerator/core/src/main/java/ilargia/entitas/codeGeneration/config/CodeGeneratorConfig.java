package ilargia.entitas.codeGeneration.config;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CodeGeneratorConfig extends AbstractConfigurableConfig {

    static final String SEARCH_PACKAGES_KEY = "CodeGeneration.CodeGenerator.SearchPkg";
    static final String PLUGINS_SCAN_KEY = "CodeGeneration.Plugins.Packages.Scan";
    static final String DATA_PROVIDERS_KEY = "CodeGeneration.DataProviders";
    static final String CODE_GENERATORS_KEY = "CodeGeneration.CodeGenerators";
    static final String POST_PROCESSORS_KEY = "CodeGeneration.PostProcessors";

    @Override
    public Properties getDefaultProperties() {
        if (!properties.containsKey(PLUGINS_SCAN_KEY))
            properties.setProperty(PLUGINS_SCAN_KEY, "ilargia.entitas.codeGeneration.plugins");
        if (!properties.containsKey(DATA_PROVIDERS_KEY)) properties.setProperty(DATA_PROVIDERS_KEY,
                String.join(", ",
                        "ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentDataProvider",
                        "ilargia.entitas.codeGeneration.plugins.dataProviders.context.ContextDataProvider",
                        "ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider"));
        if (!properties.containsKey(CODE_GENERATORS_KEY)) properties.setProperty(CODE_GENERATORS_KEY, String.join(", ",
                "ilargia.entitas.codeGeneration.plugins.generators.ComponentEntityGenerator"));
        if (properties.containsKey(POST_PROCESSORS_KEY)) properties.setProperty(POST_PROCESSORS_KEY, String.join(", ",
                "ilargia.entitas.codeGeneration.plugins.postProcessors.AddFileHeaderPostProcessor",
                "ilargia.entitas.codeGeneration.plugins.postProcessors.ConsoleWriteLinePostProcessor",
                "ilargia.entitas.codeGeneration.plugins.postProcessors.WriteToDiskPostProcessor"));

        return properties;
    }


    public List<String> getPackages() {
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(SEARCH_PACKAGES_KEY))
                .map(p-> p.trim())
                .sorted()
                .collect(Collectors.toList());

    }

    public void setPackages(List<String> pkgKey) {
        String values = pkgKey.stream()
                .sorted()
                .collect(Collectors.joining(","));

        properties.setProperty(SEARCH_PACKAGES_KEY, values);
    }


    public List<String> getPlugins() {
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(PLUGINS_SCAN_KEY))
                .map(p-> p.trim())
                .sorted()
                .collect(Collectors.toList());

    }

    public void setPlugins(List<String> pathsKey) {
        String values = pathsKey.stream()
                .sorted()
                .collect(Collectors.joining(","));

        properties.setProperty(PLUGINS_SCAN_KEY, values);
    }

    public List<String> getDataProviders() {
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(DATA_PROVIDERS_KEY))
                .map(p-> p.trim())
                .sorted()
                .collect(Collectors.toList());

    }

    public void setDataProviders(List<String> pathsdataProviders) {
        String values = pathsdataProviders.stream()
                .sorted()
                .collect(Collectors.joining(","));

        properties.setProperty(DATA_PROVIDERS_KEY, values);
    }


    public List<String> getCodeGenerators() {
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(CODE_GENERATORS_KEY))
                .map(p-> p.trim())
                .sorted()
                .collect(Collectors.toList());

    }

    public void setCodeGenerators(List<String> pathsCodeGenerators) {
        String values = pathsCodeGenerators.stream()
                .sorted()
                .collect(Collectors.joining(","));

        properties.setProperty(CODE_GENERATORS_KEY, values);
    }

    public List<String> getPostProcessors() {
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(POST_PROCESSORS_KEY))
                .map(p-> p.trim())
                .sorted()
                .collect(Collectors.toList());

    }

    public void setPostProcessors(List<String> pathsPostProcessors) {
        String values = pathsPostProcessors.stream()
                .sorted()
                .collect(Collectors.joining(","));

        properties.setProperty(POST_PROCESSORS_KEY, values);
    }

}
