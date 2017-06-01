package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.data.SourceDataFile;
import ilargia.entitas.codeGeneration.interfaces.IComponentDataProvider;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;


public class IsUniqueDataProvider implements IComponentDataProvider {


    public static String COMPONENT_IS_UNIQUE = "component_isUnique";

    public static boolean isUnique(SourceDataFile data) {
        return (boolean) data.get(COMPONENT_IS_UNIQUE);
    }

    public static void setIsUnique(SourceDataFile data, boolean isUnique) {
        data.put(COMPONENT_IS_UNIQUE, isUnique);
    }

    @Override
    public void provide(SourceDataFile data) {
        AnnotationSource<JavaClassSource> annotation = data.getFileContent().getAnnotation("Unique");
        if (annotation != null) {
            setIsUnique(data, true);
        } else {
            setIsUnique(data, false);
        }

    }

}
