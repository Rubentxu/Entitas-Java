package ilargia.entitas.codeGeneration.config;


import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class ProjectConfig {
    private final Project project;
    private final JavaPluginConvention javaConvention;

    public ProjectConfig(Project gradleProject) {
        this.project = gradleProject;
        //this.project.getPlugins().apply(JavaPlugin.class);
        javaConvention = project.getConvention().getPlugin(JavaPluginConvention.class);
    }


    public String getProjectRoot() {
        return project.getRootProject().toString();
    }

    public String getProjectName() {
        return project.getName();
    }

    public String getProjectDir() {
        return project.getProjectDir().getAbsolutePath();
    }

    public Set<File> getSrcDirs() {
        SourceSetContainer sourceSets = javaConvention.getSourceSets();
        SourceSet mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
        return mainSourceSet.getAllSource().getSrcDirs();

    }

    public String getFirtsSrcDir() {
        try {
            return getSrcDirs().iterator().next().getCanonicalPath();
        } catch (IOException e) {
            System.out.println("Error no se encuentra el directorio de fuentes");
        }
        return "";

    }

}
