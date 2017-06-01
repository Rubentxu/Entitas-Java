package ilargia.entitas.codeGeneration.gradle;


import ilargia.entitas.codeGeneration.CodeGenerator;
import ilargia.entitas.codeGeneration.config.ProjectPreferences;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class CodeGenerationTask extends DefaultTask {
    ProjectPreferences entitasProject;

    public CodeGenerationTask() {

    }

    @TaskAction
    public void run() {
        entitasProject = new EntitasGradleProject(getProject());
        CodeGenerator codeGenerator = entitasProject.getCodeGenerator();
        codeGenerator.generate();

    }
}
