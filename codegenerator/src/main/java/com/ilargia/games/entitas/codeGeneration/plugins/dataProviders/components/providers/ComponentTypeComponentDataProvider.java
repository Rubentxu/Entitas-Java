package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;

import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;


public class ComponentTypeComponentDataProvider implements IComponentDataProvider {

    public static String COMPONENT_FULL_TYPE_NAME = "component_fullTypeName";

    @Override
    public void provide(SourceDataFile data) {
        setFullTypeName(data, data.source.getCanonicalName());
    }

    public static List<String> getFullTypeName(SourceDataFile data) {
        return (List<String>) data.get(COMPONENT_FULL_TYPE_NAME);
    }

    public static void setFullTypeName(SourceDataFile data, String fullTypeName) {
        data.put(COMPONENT_FULL_TYPE_NAME, fullTypeName);
    }
}
