package ilargia.entitas.codeGeneration.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.data.SourceDataFile;


public class ComponentTypeDataProvider implements IComponentDataProvider {

    public static String COMPONENT_FULL_TYPE_NAME = "component_fullTypeName";

    @Override
    public void provide(SourceDataFile data) {
        setFullTypeName(data, data.getFileContent().getCanonicalName());
    }

    public static String getFullTypeName(SourceDataFile data) {
        return (String) data.get(COMPONENT_FULL_TYPE_NAME);
    }

    public static void setFullTypeName(SourceDataFile data, String fullTypeName) {
        data.put(COMPONENT_FULL_TYPE_NAME, fullTypeName);
    }
}
