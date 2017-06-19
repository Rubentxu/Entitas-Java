package ilargia.entitas.codeGeneration.config;


import ilargia.entitas.codeGeneration.interfaces.IConfigurable;

import java.util.Properties;

public abstract class AbstractConfigurableConfig implements IConfigurable {

    protected Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
