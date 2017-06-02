package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;


public class GenericsDataProvider implements IComponentDataProvider {

    public static String GENERIC_DATA_INFOS = "GenericInfos";

    public static List<TypeVariable> getGenericsData(ComponentData data) {
        return (List<TypeVariable>) data.get(GENERIC_DATA_INFOS);
    }

    public static void setGenericsData(ComponentData data, List<TypeVariable> generics) {
        data.put(GENERIC_DATA_INFOS, generics);
    }

    @Override
    public void provide(Class type, ComponentData data) {
        List<TypeVariable> generics = Arrays.asList(type.getTypeParameters());
        setGenericsData(data, generics);
    }

}
