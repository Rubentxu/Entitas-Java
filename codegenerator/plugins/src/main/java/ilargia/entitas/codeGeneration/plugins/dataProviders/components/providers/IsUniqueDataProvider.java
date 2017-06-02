package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGenerator.annotations.Unique;


public class IsUniqueDataProvider implements IComponentDataProvider {

    public static String COMPONENT_IS_UNIQUE = "component_isUnique";

    public static boolean isUnique(ComponentData data) {
        return (boolean) data.get(COMPONENT_IS_UNIQUE);
    }

    public static void setIsUnique(ComponentData data, boolean isUnique) {
        data.put(COMPONENT_IS_UNIQUE, isUnique);
    }

    @Override
    public void provide(Class type, ComponentData data) {
        Unique annotation = (Unique) type.getAnnotation(Unique.class);
        if (annotation != null) {
            setIsUnique(data, true);
        } else {
            setIsUnique(data, false);
        }

    }


}
