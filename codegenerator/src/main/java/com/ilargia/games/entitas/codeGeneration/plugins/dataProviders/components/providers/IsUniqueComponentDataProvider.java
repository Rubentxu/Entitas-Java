package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;


import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.Arrays;
import java.util.List;

public class IsUniqueComponentDataProvider implements IComponentDataProvider{


    public static String COMPONENT_IS_UNIQUE = "component_isUnique";

    @Override
    public void provide(JavaClassSource type, ComponentData data) {
        AnnotationSource<JavaClassSource> annotation = type.getAnnotation("Component");
        if (annotation != null) {
            Boolean isSingleEntity = Boolean.parseBoolean(annotation.getStringValue("isSingleEntity"));
            setIsUnique(data, isSingleEntity);
        }

    }

    public static boolean isUnique(ComponentData data) {
        return (boolean) data.get(COMPONENT_IS_UNIQUE);
    }

    public static void setIsUnique(ComponentData data, boolean isUnique) {
        data.put(COMPONENT_IS_UNIQUE, isUnique);
    }

}
