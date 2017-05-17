package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGeneration.SourceDataFile;


public class ShouldGenerateComponentComponentDataProvider implements IComponentDataProvider {

    public static String COMPONENT_GENERATE_COMPONENT = "component_generateComponent";
    public static String COMPONENT_OBJECT_TYPE = "component_objectType";


    @Override
    public void provide(SourceDataFile data) {
        boolean shouldGenerateComponent = !data.fileContent.hasInterface(IComponent.class);
        shouldGenerateComponent(data, shouldGenerateComponent);

        if (shouldGenerateComponent) {
            setObjectType(data, data.fileContent.getQualifiedName());
        }

    }

    public static boolean shouldGenerateComponent(SourceDataFile data) {
        return (boolean) data.get(COMPONENT_GENERATE_COMPONENT);
    }

    public static void shouldGenerateComponent(SourceDataFile data, boolean generate) {
        data.put(COMPONENT_GENERATE_COMPONENT, generate);
    }

    public static String getObjectType(SourceDataFile data) {
        return (String) data.get(COMPONENT_OBJECT_TYPE);
    }

    public static void setObjectType(SourceDataFile data, String type) {
        data.put(COMPONENT_OBJECT_TYPE, type);
    }

}
