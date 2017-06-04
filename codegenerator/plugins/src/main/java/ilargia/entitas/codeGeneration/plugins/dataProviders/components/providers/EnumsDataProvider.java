package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.JavaClassSource;

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
    public void provide(ComponentData data) {
        List<String> enums = data.getSource().getNestedTypes().stream()
                .filter(method -> method.isPublic())
                .filter(method -> method.isEnum())
                .map(method -> method.getCanonicalName())
                .collect(Collectors.toList());
        setEnumData(data, enums);
    }

}
