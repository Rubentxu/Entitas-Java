package ilargia.entitas.gradle;


import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class CodeGenerationTask extends DefaultTask {
    @TaskAction
    public void run() {
        CodeGenerationPluginExtension extension = getProject().getExtensions().findByType(CodeGenerationPluginExtension.class);
        if (extension == null) {
            extension = new CodeGenerationPluginExtension();
        }

        String config = extension.getConfigCodeGen();
        System.out.println(config);
    }
}
