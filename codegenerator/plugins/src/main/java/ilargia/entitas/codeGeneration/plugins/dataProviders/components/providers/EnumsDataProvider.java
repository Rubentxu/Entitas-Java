package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EnumsDataProvider implements IComponentDataProvider {

    public static String ENUMS_DATA_INFOS = "EnumInfos";

    public static List<String> getEnumData(ComponentData data) {
        return (List<String>) data.get(ENUMS_DATA_INFOS);
    }

    public static void setEnumData(ComponentData data, List<String> enums) {
        data.put(ENUMS_DATA_INFOS, enums);
    }

    @Override
    public void provide(Class type, ComponentData data) {
        List<String> enums = Stream.of(type.getFields())
                .map(f -> f.getType())
                .filter(c -> Modifier.isPublic(c.getModifiers()))
                .filter(c -> c.isEnum())
                .map(method -> method.getCanonicalName())
                .collect(Collectors.toList());
        setEnumData(data, enums);
    }

}
