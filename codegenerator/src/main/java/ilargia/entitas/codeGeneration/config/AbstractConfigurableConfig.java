package ilargia.entitas.codeGeneration.config;


import ilargia.entitas.codeGeneration.interfaces.IConfigurable;

import java.util.Properties;

public class AbstractConfigurableConfig implements IConfigurable {

    protected Properties properties;

    @Override
    public Properties getDefaultProperties() {
        return properties;
    }

    @Override
    public void configure(Properties properties) {
        this.properties = properties;
    }
}
