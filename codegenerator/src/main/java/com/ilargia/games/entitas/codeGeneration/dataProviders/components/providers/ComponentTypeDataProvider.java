package com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers;

import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile;

import java.util.List;


public class ComponentTypeDataProvider implements IComponentDataProvider {

    public static String COMPONENT_FULL_TYPE_NAME = "component_fullTypeName";

    @Override
    public void provide(SourceDataFile data) {
        setFullTypeName(data, data.source.getCanonicalName());
    }

    public static String getFullTypeName(SourceDataFile data) {
        return (String) data.get(COMPONENT_FULL_TYPE_NAME);
    }

    public static void setFullTypeName(SourceDataFile data, String fullTypeName) {
        data.put(COMPONENT_FULL_TYPE_NAME, fullTypeName);
    }
}
