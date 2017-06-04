package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConstructorDataProvider implements IComponentDataProvider {

    public static String CONSTRUCTOR_INFOS = "constructorInfos";

    public static List<MethodSource<JavaClassSource>> getConstructorData(ComponentData data) {
        return (List<MethodSource<JavaClassSource>>) data.get(CONSTRUCTOR_INFOS);
    }

    public static void setConstructorData(ComponentData data, List<MethodSource<JavaClassSource>> contructores) {
        data.put(CONSTRUCTOR_INFOS, contructores);
    }

    @Override
    public void provide(ComponentData data) {
        List<MethodSource<JavaClassSource>> contructores = data.getSource().getMethods().stream()
                .filter(method -> method.isPublic())
                .filter(method -> method.isConstructor())
                .collect(Collectors.toList());
        setConstructorData(data, contructores);
    }

}
