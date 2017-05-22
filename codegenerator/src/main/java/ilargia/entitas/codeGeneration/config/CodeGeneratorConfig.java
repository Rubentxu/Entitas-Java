package ilargia.entitas.codeGeneration.config;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CodeGeneratorConfig extends AbstractConfigurableConfig {


    static String SEARCH_PATHS_KEY = "Entitas.CodeGeneration.CodeGenerator.SearchPaths";
    static String PLUGINS_PATHS_KEY = "Entitas.CodeGeneration.CodeGenerator.Plugins";

    static String DATA_PROVIDERS_KEY = "Entitas.CodeGeneration.CodeGenerator.DataProviders";
    static String CODE_GENERATORS_KEY = "Entitas.CodeGeneration.CodeGenerator.CodeGenerators";
    static String POST_PROCESSORS_KEY = "Entitas.CodeGeneration.CodeGenerator.PostProcessors";

    @Override
    public Properties getDefaultProperties() {
        properties.setProperty(SEARCH_PATHS_KEY, "");
        properties.setProperty(PLUGINS_PATHS_KEY, "Entitas.CodeGeneration.Plugins, Entitas.VisualDebugging.CodeGeneration.Plugins");
        properties.setProperty(DATA_PROVIDERS_KEY, "");
        properties.setProperty(CODE_GENERATORS_KEY, "");
        properties.setProperty(POST_PROCESSORS_KEY, "");

        return properties;
    }


    public List<String> getSearchPaths() {
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(SEARCH_PATHS_KEY))
                .sorted()
                .collect(Collectors.toList());

    }

    public void setSearchPaths(List<String> searchPaths) {
        String values = searchPaths.stream()
                .sorted()
                .collect(Collectors.joining(","));

        properties.setProperty(SEARCH_PATHS_KEY, values);
    }

    public List<String> getPlugins() {
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(PLUGINS_PATHS_KEY))
                .sorted()
                .collect(Collectors.toList());

    }

    public void setPlugins(List<String> pathsKey) {
        String values = pathsKey.stream()
                .sorted()
                .collect(Collectors.joining(","));

        properties.setProperty(PLUGINS_PATHS_KEY, values);
    }

    public List<String> getDataProviders() {
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(DATA_PROVIDERS_KEY))
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
