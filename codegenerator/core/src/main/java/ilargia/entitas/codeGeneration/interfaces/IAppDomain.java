package ilargia.entitas.codeGeneration.interfaces;

import ilargia.entitas.codeGeneration.CodeGenerator;

import java.io.File;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Ruben on 31/05/2017.
 */
public interface IAppDomain {

    CodeGenerator getCodeGenerator();

    String getAppRoot();

    String getAppName();

    String getAppDir();

    Set<File> getSrcDirs();

    String getFirtsSrcDir();

    boolean hasProperties();

    Properties loadProperties();

    void saveProperties(Properties properties);

}
