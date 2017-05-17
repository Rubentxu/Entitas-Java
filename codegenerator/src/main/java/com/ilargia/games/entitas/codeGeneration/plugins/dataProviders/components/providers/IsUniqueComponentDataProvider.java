package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;


import com.ilargia.games.entitas.codeGeneration.SourceDataFile;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;


public class IsUniqueComponentDataProvider implements IComponentDataProvider {


    public static String COMPONENT_IS_UNIQUE = "component_isUnique";

    @Override
    public void provide(SourceDataFile data) {
        AnnotationSource<JavaClassSource> annotation = data.fileContent.getAnnotation("IsUnique");
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
