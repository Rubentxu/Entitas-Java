package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConstructorDataProvider implements IComponentDataProvider {

    public static String CONSTRUCTOR_INFOS = "constructorInfos";

    public static List<Constructor> getConstructorData(ComponentData data) {
        return (List<Constructor>) data.get(CONSTRUCTOR_INFOS);
    }

    public static void setConstructorData(ComponentData data, List<Constructor> contructores) {
        data.put(CONSTRUCTOR_INFOS, contructores);
    }

    @Override
    public void provide(Class type, ComponentData data) {
        List<Constructor> contructores = Stream.of(type.getConstructors())
                .filter(c -> Modifier.isPublic(c.getModifiers()))
//                .filter(method -> method.getParameters().size() > 0)
                .collect(Collectors.toList());
        setConstructorData(data, contructores);
    }

}
