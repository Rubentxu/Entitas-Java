package ilargia.entitas.codeGeneration.config;


import java.util.Properties;

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
