package ilargia.entitas.codeGeneration.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.data.SourceDataFile;

import java.util.List;
import java.util.stream.Collectors;


public class EnumsDataProvider implements IComponentDataProvider {

    public static String ENUMS_DATA_INFOS = "EnumInfos";

    @Override
    public void provide(SourceDataFile data) {
        List<String> enums = data.getFileContent().getNestedTypes().stream()
                .filter(method -> method.isPublic())
                .filter(method -> method.isEnum())
                .map(method -> method.getCanonicalName())
                .collect(Collectors.toList());
        setEnumData(data, enums);
    }

    public static List<String> getEnumData(SourceDataFile data) {
        return (List<String>) data.get(ENUMS_DATA_INFOS);
    }

    public static void setEnumData(SourceDataFile data, List<String> enums) {
        data.put(ENUMS_DATA_INFOS, enums);
    }

}
