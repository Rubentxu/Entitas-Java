package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.config.ContextNamesConfig;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGenerator.annotations.Unique;

import java.util.Properties;

public class CustomPrefixDataProvider implements IComponentDataProvider, IConfigurable {

    public static String COMPONENT_CUSTOM_PREFIX = "component_customPrefix";
    ContextNamesConfig _contextNamesConfig = new ContextNamesConfig();

    public static void setCustomComponentPrefix(ComponentData data, String prefix) {
        data.put(COMPONENT_CUSTOM_PREFIX, prefix);
    }

    public static String getCustomComponentPrefix(ComponentData data) {
        return (String) data.get(COMPONENT_CUSTOM_PREFIX);
    }

    @Override
    public Properties getDefaultProperties() {
        return _contextNamesConfig.getDefaultProperties();
    }

    @Override
    public void configure(Properties properties) {
        _contextNamesConfig.configure(properties);
    }

    @Override
    public void provide(Class type, ComponentData data) {
        setCustomComponentPrefix(data, getUniqueComponentPrefix(type));
    }

    private String getUniqueComponentPrefix(Class clazz) {
        Unique annotation = (Unique) clazz.getAnnotation(Unique.class);
        if (annotation != null) {
            return annotation.prefix();
        }
        return "is";
    }

}
