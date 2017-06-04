package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.TypeVariableSource;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;


public class GenericsDataProvider implements IComponentDataProvider {

    public static String GENERIC_DATA_INFOS = "GenericInfos";

    public static List<TypeVariableSource<JavaClassSource>> getGenericsData(ComponentData data) {
        return (List<TypeVariableSource<JavaClassSource>>) data.get(GENERIC_DATA_INFOS);
    }

    public static void setGenericsData(ComponentData data, List<TypeVariableSource<JavaClassSource>> generics) {
        data.put(GENERIC_DATA_INFOS, generics);
    }

    @Override
    public void provide(ComponentData data) {
        List<TypeVariableSource<JavaClassSource>> generics = data.getSource().getOrigin().getTypeVariables();
        setGenericsData(data, generics);
    }

}
