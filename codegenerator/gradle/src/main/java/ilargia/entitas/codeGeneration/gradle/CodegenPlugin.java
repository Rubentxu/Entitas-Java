package ilargia.entitas.codeGeneration.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class CodegenPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("EntitasSetting", CodeGenerationPluginExtension.class);
        project.getTasks().create("codegen", CodeGenerationTask.class);
    }
}
