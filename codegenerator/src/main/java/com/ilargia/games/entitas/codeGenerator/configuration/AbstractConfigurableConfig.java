package com.ilargia.games.entitas.codeGenerator.configuration;


import com.ilargia.games.entitas.codeGenerator.interfaces.configuration.IConfigurable;

import java.util.Properties;

public class AbstractConfigurableConfig implements IConfigurable{

    protected Properties properties;

    public AbstractConfigurableConfig(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Properties getDefaultProperties() {
        return properties;
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public void configure(Properties properties) {
        this.properties = properties;
    }
}
