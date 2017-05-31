package ilargia.entitas.codeGeneration.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.config.ContextNamesConfig;
import ilargia.entitas.codeGeneration.data.SourceDataFile;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ContextsDataProvider implements IComponentDataProvider, IConfigurable {

    public static String COMPONENT_CONTEXTS = "component_contexts";
    ContextNamesConfig _contextNamesConfig = new ContextNamesConfig();

    public static List<String> getContextNames(SourceDataFile data) {
        return (List<String>) data.get(COMPONENT_CONTEXTS);
    }

    public static void setContextNames(SourceDataFile data, List<String> contextNames) {
        data.put(COMPONENT_CONTEXTS, contextNames);
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
    public void provide(SourceDataFile data) {
        List<String> contextNames = getContextNamesOrDefault(data.getFileContent());
        setContextNames(data, contextNames);
    }

    public List<String> extractContextNames(JavaClassSource type) {
        AnnotationSource<JavaClassSource> annotation = type.getAnnotation("Contexts");
        if (annotation != null) {
            return (annotation.toString().contains("names"))
                    ? Arrays.asList(annotation.getStringArrayValue("names"))
                    : null;
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
