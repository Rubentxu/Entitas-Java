package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;


import com.ilargia.games.entitas.codeGeneration.plugins.config.ContextNamesConfig;
import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import com.ilargia.games.entitas.codeGenerator.interfaces.configuration.IConfigurable;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ContextsComponentDataProvider implements IComponentDataProvider, IConfigurable {

    public static String COMPONENT_CONTEXTS = "component_contexts";
    ContextNamesConfig _contextNamesConfig = new ContextNamesConfig();

    @Override
    public Properties getDefaultProperties() {
        return null;
    }

    @Override
    public void configure(Properties properties) {
        _contextNamesConfig.configure(properties);
    }

    @Override
    public void provide(JavaClassSource type, ComponentData data) {
        List<String> contextNames = getContextNamesOrDefault(type);
        setContextNames(data, contextNames);
    }

    public List<String> getContextNames(JavaClassSource type) {
        AnnotationSource<JavaClassSource> annotation = type.getAnnotation("Component");
        if (annotation != null) {
            List<String> poolNames = (annotation.toString().contains("pools"))
                    ? Arrays.asList(annotation.getStringArrayValue("pools"))
                    : null;
        }
        return new ArrayList<>();
    }

    public List<String> getContextNamesOrDefault(JavaClassSource type) {
        List<String> contextNames = getContextNames(type);
        if (contextNames.size() == 0) {
            contextNames = _contextNamesConfig.getContextNames();
        }
        return contextNames;
    }

    public static List<String> getContextNames(ComponentData data) {
        return (List<String>) data.get(COMPONENT_CONTEXTS);
    }

    public static void setContextNames(ComponentData data, List<String> contextNames) {
        data.put(COMPONENT_CONTEXTS, contextNames);
    }

}
