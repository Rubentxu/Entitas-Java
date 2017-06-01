package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.data.SourceDataFile;
import ilargia.entitas.codeGeneration.interfaces.IComponentDataProvider;
import ilargia.entitas.codeGenerator.annotations.DontGenerate;

public class ShouldGenerateMethodsDataProvider implements IComponentDataProvider {

    public static String COMPONENT_GENERATE_METHODS = "component_generateMethods";

    public static boolean shouldGenerateMethods(SourceDataFile data) {
        return (boolean) data.get(COMPONENT_GENERATE_METHODS);
    }

    public static void shouldGenerateMethods(SourceDataFile data, boolean generate) {
        data.put(COMPONENT_GENERATE_METHODS, generate);
    }

    @Override
    public void provide(SourceDataFile data) {
        boolean generate = !data.getFileContent().hasAnnotation(DontGenerate.class);
        shouldGenerateMethods(data, generate);
    }


}
