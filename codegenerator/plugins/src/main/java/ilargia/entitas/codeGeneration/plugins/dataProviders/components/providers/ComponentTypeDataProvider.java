package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class ComponentTypeDataProvider implements IComponentDataProvider {

    public static String COMPONENT_FULL_TYPE_NAME = "component_fullTypeName";
    public static String COMPONENT_TYPE_NAME = "component_TypeName";
    public static String COMPONENT_TYPE_NAME_SUFFIX = "component_TypeNameSuffix";

    public static String getFullTypeName(ComponentData data) {
        return (String) data.get(COMPONENT_FULL_TYPE_NAME);
    }

    public static void setFullTypeName(ComponentData data, String fullTypeName) {
        data.put(COMPONENT_FULL_TYPE_NAME, fullTypeName);
    }

    public static String getTypeName(ComponentData data) {
        return (String) data.get(COMPONENT_TYPE_NAME);
    }

    public static void setTypeName(ComponentData data, String typeName) {
        data.put(COMPONENT_TYPE_NAME, typeName);
    }

    public static String getTypeNameSuffix(ComponentData data) {
        return (String) data.get(COMPONENT_TYPE_NAME_SUFFIX);
    }

    public static void setTypeNameSuffix(ComponentData data, String typeNameSuffix) {
        data.put(COMPONENT_TYPE_NAME_SUFFIX, typeNameSuffix);
    }

    @Override
    public void provide(ComponentData data) {
        setFullTypeName(data, data.getSource().getCanonicalName());
        setTypeName(data, data.getSource().getName());
        setTypeNameSuffix(data, data.getSource().getName()+"Component");
    }
}
