package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.config.ContextNamesConfig;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGenerator.annotations.Contexts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ContextsDataProvider implements IComponentDataProvider, IConfigurable {

    public static String COMPONENT_CONTEXTS = "component_contexts";
    ContextNamesConfig _contextNamesConfig = new ContextNamesConfig();

    public static void setContextNames(ComponentData data, List<String> contextNames) {
        data.put(COMPONENT_CONTEXTS, contextNames);
    }

    public static List<String> getContextNames(ComponentData data) {
        return (List<String>) data.get(COMPONENT_CONTEXTS);
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
        List<String> contextNames = getContextNamesOrDefault(type);
        setContextNames(data, contextNames);
    }

    public List<String> extractContextNames(Class clazz) {
        Contexts annotation = (Contexts) clazz.getAnnotation(Contexts.class);
        if (annotation != null) {
            return Arrays.asList(annotation.names());
        }
        return new ArrayList<>();
    }

    public List<String> getContextNamesOrDefault(Class type) {
        List<String> contextNames = extractContextNames(type);
        if (contextNames.size() == 0) {
            contextNames = _contextNamesConfig.getContextNames();
        }
        return contextNames;
    }

}
