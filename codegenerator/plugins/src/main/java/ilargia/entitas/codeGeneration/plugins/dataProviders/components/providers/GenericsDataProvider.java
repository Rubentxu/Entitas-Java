package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.data.SourceDataFile;
import ilargia.entitas.codeGeneration.interfaces.IComponentDataProvider;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.TypeVariableSource;

import java.util.List;


public class GenericsDataProvider implements IComponentDataProvider {

    public static String GENERIC_DATA_INFOS = "GenericInfos";

    public static List<TypeVariableSource<JavaClassSource>> getGenericsData(SourceDataFile data) {
        return (List<TypeVariableSource<JavaClassSource>>) data.get(GENERIC_DATA_INFOS);
    }

    public static void setGenericsData(SourceDataFile data, List<TypeVariableSource<JavaClassSource>> generics) {
        data.put(GENERIC_DATA_INFOS, generics);
    }

    @Override
    public void provide(SourceDataFile data) {
        List<TypeVariableSource<JavaClassSource>> generics = data.getFileContent().getOrigin().getTypeVariables();
        setGenericsData(data, generics);
    }

}
