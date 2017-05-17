package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;

import com.ilargia.games.entitas.codeGeneration.SourceDataFile;
import com.ilargia.games.entitas.codeGeneration.annotations.DontGenerate;

public class ShouldGenerateMethodsComponentDataProvider implements IComponentDataProvider {

    public static String COMPONENT_GENERATE_METHODS = "component_generateMethods";


    @Override
    public void provide(SourceDataFile data) {
        boolean generate = data.fileContent.hasAnnotation(DontGenerate.class);
        shouldGenerateMethods(data, generate);
    }

    public static boolean shouldGenerateMethods(SourceDataFile data) {
        return (boolean) data.get(COMPONENT_GENERATE_METHODS);
    }

    public static void shouldGenerateMethods(SourceDataFile data, boolean generate) {
        data.put(COMPONENT_GENERATE_METHODS, generate);
    }


}
