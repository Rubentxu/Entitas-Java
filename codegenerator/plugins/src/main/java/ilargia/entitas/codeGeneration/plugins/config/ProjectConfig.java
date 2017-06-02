package ilargia.entitas.codeGeneration.plugins.config;


import ilargia.entitas.codeGeneration.config.AbstractConfigurableConfig;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class ProjectConfig extends AbstractConfigurableConfig {
    static String PROJECT_PATH_KEY  = "CodeGeneration.Plugins.ProjectPath";

    public String getProjectPath() {
        return properties.getProperty(PROJECT_PATH_KEY);
    }
}
