package ilargia.entitas.codeGeneration.config;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CodeGeneratorConfig extends AbstractConfigurableConfig {

    static String PLUGINS_PATHS_KEY = "Entitas.CodeGeneration.Plugins";
    static String DATA_PROVIDERS_KEY = "Entitas.CodeGeneration.DataProviders";
    static String CODE_GENERATORS_KEY = "Entitas.CodeGeneration.CodeGenerators";
    static String POST_PROCESSORS_KEY = "Entitas.CodeGeneration.PostProcessors";

    @Override
    public Properties getDefaultProperties() {
        if(!properties.containsKey(PLUGINS_PATHS_KEY)) properties.setProperty(PLUGINS_PATHS_KEY, "");
        if(!properties.containsKey(DATA_PROVIDERS_KEY)) properties.setProperty(DATA_PROVIDERS_KEY, "");
        if(!properties.containsKey(CODE_GENERATORS_KEY)) properties.setProperty(CODE_GENERATORS_KEY, "");
        if(properties.containsKey(POST_PROCESSORS_KEY)) properties.setProperty(POST_PROCESSORS_KEY, "");

        return properties;
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
