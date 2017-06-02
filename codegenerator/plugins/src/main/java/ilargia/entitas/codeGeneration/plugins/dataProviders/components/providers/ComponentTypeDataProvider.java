package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;

public class ComponentTypeDataProvider implements IComponentDataProvider {

    public static String COMPONENT_FULL_TYPE_NAME = "component_fullTypeName";

    public static String getFullTypeName(ComponentData data) {
        return (String) data.get(COMPONENT_FULL_TYPE_NAME);
    }

    public static void setFullTypeName(ComponentData data, String fullTypeName) {
        data.put(COMPONENT_FULL_TYPE_NAME, fullTypeName);
    }

    @Override
    public void provide(Class type, ComponentData data) {
        setFullTypeName(data, type.getCanonicalName());
    }
}
