package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.JavaClassSource;


public class ShouldGenerateComponentComponentDataProvider implements IComponentDataProvider {


    public static String COMPONENT_GENERATE_COMPONENT = "component_generateComponent";
    public static String COMPONENT_OBJECT_TYPE = "component_objectType";


    @Override
    public void provide(JavaClassSource type, ComponentData data) {
        boolean shouldGenerateComponent = !type.hasInterface(IComponent.class);
        shouldGenerateComponent(data, shouldGenerateComponent);

        if (shouldGenerateComponent) {
            setObjectType(data, type.getQualifiedName());
        }

    }

    public static boolean shouldGenerateComponent(ComponentData data) {
        return (boolean) data.get(COMPONENT_GENERATE_COMPONENT);
    }

    public static void shouldGenerateComponent(ComponentData data, boolean generate) {
        data.put(COMPONENT_GENERATE_COMPONENT, generate);
    }

    public static String getObjectType(ComponentData data) {
        return (String) data.get(COMPONENT_OBJECT_TYPE);
    }

    public static void setObjectType(ComponentData data, String type) {
        data.put(COMPONENT_OBJECT_TYPE, type);
    }

}
