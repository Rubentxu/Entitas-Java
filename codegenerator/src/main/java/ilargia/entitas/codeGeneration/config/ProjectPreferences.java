package ilargia.entitas.codeGeneration.config;

import ilargia.entitas.codeGeneration.CodeGenerator;

import java.io.File;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Ruben on 31/05/2017.
 */
public interface ProjectPreferences {

    CodeGenerator getCodeGenerator();

    String getProjectRoot();

    String getProjectName();

    String getProjectDir();

    Set<File> getSrcDirs();

    String getFirtsSrcDir();

    boolean hasProperties();

    Properties loadProperties();

    void saveProperties(Properties properties);

}
