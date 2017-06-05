package ilargia.entitas.fixtures;

import ilargia.entitas.codeGeneration.interfaces.IAppDomain;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TestProject implements IAppDomain {
    private final Project project;
    private final JavaPluginConvention javaConvention;


    public TestProject(Project gradleProject) {
        this.project = gradleProject;
        this.project.getPlugins().apply(JavaPlugin.class);
        javaConvention = project.getConvention().getPlugin(JavaPluginConvention.class);

    }

    @Override
    public String getAppRoot() {
        return project.getRootProject().toString();
    }

    @Override
    public String getAppName() {
        return project.getName();
    }

    @Override
    public String getAppDir() {
        return project.getProjectDir().getAbsolutePath();
    }

    @Override
    public List<String> getSrcDirs() {
        SourceSetContainer sourceSets = javaConvention.getSourceSets();
        SourceSet mainSourceSet = sourceSets.getByName(SourceSet.TEST_SOURCE_SET_NAME);
        return mainSourceSet.getAllSource().getSrcDirs().stream().map(f -> {
            try {
                return f.getCanonicalPath();
            } catch (IOException e) {
                return "";
            }
        }).collect(Collectors.toList());
    }

}