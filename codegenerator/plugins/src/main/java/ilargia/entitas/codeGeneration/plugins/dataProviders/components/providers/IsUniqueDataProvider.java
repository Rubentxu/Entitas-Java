package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGenerator.annotations.Unique;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;


public class IsUniqueDataProvider implements IComponentDataProvider {

    public static String COMPONENT_IS_UNIQUE = "component_isUnique";

    public static boolean isUnique(ComponentData data) {
        return (boolean) data.get(COMPONENT_IS_UNIQUE);
    }

    public static void setIsUnique(ComponentData data, boolean isUnique) {
        data.put(COMPONENT_IS_UNIQUE, isUnique);
    }

    @Override
    public void provide(ComponentData data) {
        setIsUnique(data, data.getSource().hasAnnotation(Unique.class));

    }


}
