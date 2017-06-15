package ilargia.entitas.codeGeneration.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class CodegenPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        CodeGenerationPluginExtension config = project.getExtensions().create("entitas", CodeGenerationPluginExtension.class);
        project.getTasks().create("codegen", CodeGenerationTask.class);
    }
}
