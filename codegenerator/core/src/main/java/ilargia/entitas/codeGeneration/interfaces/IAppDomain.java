package ilargia.entitas.codeGeneration.interfaces;

import ilargia.entitas.codeGeneration.CodeGenerator;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public interface IAppDomain {

    CodeGenerator getCodeGenerator();

    String getAppRoot();

    String getAppName();

    String getAppDir();

    List<String> getSrcDirs();

    boolean hasProperties();

    Properties loadProperties();

    void saveProperties(Properties properties);

}
