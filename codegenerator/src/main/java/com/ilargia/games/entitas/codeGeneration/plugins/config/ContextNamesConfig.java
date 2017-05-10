package com.ilargia.games.entitas.codeGeneration.plugins.config;


import com.ilargia.games.entitas.codeGenerator.configuration.AbstractConfigurableConfig;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContextNamesConfig extends AbstractConfigurableConfig {
    static String CONTEXTS_KEY = "Entitas.CodeGeneration.Plugins.Contexts";

    @Override
    public Properties getDefaultProperties() {
        properties.setProperty(CONTEXTS_KEY, "Game, Input");
        return properties;
    }

    public List<String> getContextNames() {
        return Pattern.compile(",")
                .splitAsStream(properties.getProperty(CONTEXTS_KEY))
                .sorted()
                .collect(Collectors.toList());
    }
}
