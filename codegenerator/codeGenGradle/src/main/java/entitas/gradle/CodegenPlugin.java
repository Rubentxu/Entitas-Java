package entitas.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Properties;

public class CodegenPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        Properties properties = new Properties();
        CodeGenerationPluginExtension config = project.getExtensions().create("entitas", CodeGenerationPluginExtension.class);
        config.configure(properties);
        project.getTasks().create("codegen", CodeGenerationTask.class);
    }
}
