package com.ilargia.games.entitas.codeGeneration.plugins.config;


import com.ilargia.games.entitas.codeGenerator.configuration.AbstractConfigurableConfig;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProjectPathConfig extends AbstractConfigurableConfig {
    static String PROJECT_PATH_KEY = "Entitas.CodeGeneration.Plugins.ProjectPath";

    @Override
    public Properties getDefaultProperties() {
        properties.setProperty(PROJECT_PATH_KEY, "./");
        return properties;
    }

    public String projectPath() {
        return properties.getProperty(PROJECT_PATH_KEY);
    }
}
