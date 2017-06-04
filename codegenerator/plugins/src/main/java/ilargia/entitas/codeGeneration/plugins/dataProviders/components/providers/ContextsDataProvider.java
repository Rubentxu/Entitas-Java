package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.config.ContextNamesConfig;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGenerator.annotations.Contexts;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

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
    public void provide(ComponentData data) {
        List<String> contextNames = getContextNamesOrDefault(data.getSource());
        setContextNames(data, contextNames);
    }

    public List<String> extractContextNames(JavaClassSource clazz) {
        AnnotationSource<JavaClassSource> annotation =  clazz.getAnnotation(Contexts.class);
        if (annotation != null) {
            return Arrays.asList(annotation.getStringArrayValue("names"));
        }
        return new ArrayList<>();
    }

    public List<String> getContextNamesOrDefault(JavaClassSource type) {
        List<String> contextNames = extractContextNames(type);
        if (contextNames.size() == 0) {
            contextNames = _contextNamesConfig.getContextNames();
        }
        return contextNames;
    }

}
