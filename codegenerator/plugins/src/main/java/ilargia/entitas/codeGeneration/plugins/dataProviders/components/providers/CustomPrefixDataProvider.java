package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.config.ContextNamesConfig;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGenerator.annotations.Unique;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

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
    public void provide(ComponentData data) {
        setCustomComponentPrefix(data, getUniqueComponentPrefix(data.getSource()));
    }

    private String getUniqueComponentPrefix(JavaClassSource clazz) {
        AnnotationSource<JavaClassSource> annotation = clazz.getAnnotation(Unique.class);
        if (annotation != null) {
            return annotation.getStringValue("prefix");
        }
        return "is";
    }

}
