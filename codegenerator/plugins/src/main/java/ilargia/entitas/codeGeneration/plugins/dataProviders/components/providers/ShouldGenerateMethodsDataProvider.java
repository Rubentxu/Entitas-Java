package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGenerator.annotations.DontGenerate;

public class ShouldGenerateMethodsDataProvider implements IComponentDataProvider {

    public static String COMPONENT_GENERATE_METHODS = "component_generateMethods";

    public static boolean shouldGenerateMethods(CodeGeneratorData data) {
        return (boolean) data.get(COMPONENT_GENERATE_METHODS);
    }

    public static void shouldGenerateMethods(CodeGeneratorData data, boolean generate) {
        data.put(COMPONENT_GENERATE_METHODS, generate);
    }

    @Override
    public void provide(ComponentData data) {
        boolean generate = data.getSource().getAnnotation(DontGenerate.class) == null;
        shouldGenerateMethods(data, generate);
    }
}
