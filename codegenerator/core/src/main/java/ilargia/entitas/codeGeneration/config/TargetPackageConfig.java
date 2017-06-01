package ilargia.entitas.codeGeneration.config;


import java.util.Properties;

public class TargetPackageConfig extends AbstractConfigurableConfig {
    public static String TARGET_PACKAGE_KEY = "Entitas.CodeGeneration.TargetPackage";

    @Override
    public Properties getDefaultProperties() {
        if (!properties.containsKey(TARGET_PACKAGE_KEY))
            properties.setProperty(TARGET_PACKAGE_KEY, "entitas.generated");
        return properties;
    }

    public String targetPackage() {
        return properties.getProperty(TARGET_PACKAGE_KEY);
    }

}
