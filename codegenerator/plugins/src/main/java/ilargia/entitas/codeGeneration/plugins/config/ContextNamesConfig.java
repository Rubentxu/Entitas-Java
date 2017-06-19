package ilargia.entitas.codeGeneration.plugins.config;


import ilargia.entitas.codeGeneration.config.AbstractConfigurableConfig;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContextNamesConfig extends AbstractConfigurableConfig {
    static String CONTEXTS_KEY = "CodeGeneration.Contexts";

    @Override
    public Properties defaultProperties() {
        if (!properties.containsKey(CONTEXTS_KEY)) properties.setProperty(CONTEXTS_KEY, "Core");
        return properties;
    }

    public List<String> getContextNames() {
        if(!properties.contains(CONTEXTS_KEY)) defaultProperties();
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(CONTEXTS_KEY))
                .sorted()
                .collect(Collectors.toList());
    }
}
