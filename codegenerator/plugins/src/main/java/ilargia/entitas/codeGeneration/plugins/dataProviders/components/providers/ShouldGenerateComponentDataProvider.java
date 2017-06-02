package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;

import java.util.Arrays;


public class ShouldGenerateComponentDataProvider implements IComponentDataProvider {

    public static String COMPONENT_GENERATE_COMPONENT = "component_generateComponent";
    public static String COMPONENT_OBJECT_TYPE = "component_objectType";

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

    @Override
    public void provide(Class type, ComponentData data) {
        boolean shouldGenerateComponent = !Arrays.stream(type.getInterfaces())
                .anyMatch(t -> t.equals(IComponent.class));
        shouldGenerateComponent(data, shouldGenerateComponent);

        if (shouldGenerateComponent) {
            setObjectType(data, type.getCanonicalName());
        }

    }

}
