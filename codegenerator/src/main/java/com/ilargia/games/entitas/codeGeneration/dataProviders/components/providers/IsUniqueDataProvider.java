package com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers;


import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;


public class IsUniqueDataProvider implements IComponentDataProvider {


    public static String COMPONENT_IS_UNIQUE = "component_isUnique";

    @Override
    public void provide(SourceDataFile data) {
        AnnotationSource<JavaClassSource> annotation = data.source.getAnnotation("IsUnique");
        if (annotation != null) {
            setIsUnique(data, true);
        }

    }

    public static boolean isUnique(SourceDataFile data) {
        return (boolean) data.get(COMPONENT_IS_UNIQUE);
    }

    public static void setIsUnique(SourceDataFile data, boolean isUnique) {
        data.put(COMPONENT_IS_UNIQUE, isUnique);
    }

}
