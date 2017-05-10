package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;

import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;


public class ComponentTypeComponentDataProvider implements IComponentDataProvider {

    public static String COMPONENT_FULL_TYPE_NAME = "component_fullTypeName";

    @Override
    public void provide(JavaClassSource type, ComponentData data) {
        setFullTypeName(data, type.getCanonicalName());
    }

    public static List<String> getFullTypeName(ComponentData data) {
        return (List<String>) data.get(COMPONENT_FULL_TYPE_NAME);
    }

    public static void setFullTypeName(ComponentData data, String fullTypeName) {
        data.put(COMPONENT_FULL_TYPE_NAME, fullTypeName);
    }
}
