package com.ilargia.games.entitas.codeGeneration.plugins.config;


import com.ilargia.games.entitas.codeGenerator.configuration.AbstractConfigurableConfig;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TargetDirectoryConfig extends AbstractConfigurableConfig {
    static String TARGET_DIRECTORY_KEY = "Entitas.CodeGeneration.Plugins.TargetDirectory";

    @Override
    public Properties getDefaultProperties() {
        properties.setProperty(TARGET_DIRECTORY_KEY, "./generated");
        return properties;
    }

    public String targetDirectory() {
       return properties.getProperty(TARGET_DIRECTORY_KEY);
    }

}
