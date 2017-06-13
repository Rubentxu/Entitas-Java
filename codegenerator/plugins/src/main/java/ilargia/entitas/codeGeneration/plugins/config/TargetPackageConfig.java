package ilargia.entitas.codeGeneration.plugins.config;


import ilargia.entitas.codeGeneration.config.AbstractConfigurableConfig;

import java.util.Properties;

public class TargetPackageConfig extends AbstractConfigurableConfig {
    public static String TARGET_PACKAGE_KEY = "CodeGeneration.TargetPackage";

    @Override
    public Properties getDefaultProperties() {
        if (!properties.containsKey(TARGET_PACKAGE_KEY))
            properties.setProperty(TARGET_PACKAGE_KEY, "entitas.generated");
        return properties;
    }

    public String getTargetPackage() {
        return properties.getProperty(TARGET_PACKAGE_KEY);
    }

    public void setTargetPackage(String target) {
        properties.setProperty(TARGET_PACKAGE_KEY, target);
    }

}
