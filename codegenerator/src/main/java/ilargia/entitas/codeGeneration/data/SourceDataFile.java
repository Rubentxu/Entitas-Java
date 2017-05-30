package ilargia.entitas.codeGeneration.data;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import java.util.HashMap;

public class SourceDataFile extends HashMap<String, Object> {

    private String fileName;
    private String componentsDir;
    private String subDir;
    private JavaClassSource fileContent;


    public SourceDataFile(String fileName, String componentsDir, String subDir, JavaClassSource fileContent) {
        this.fileName = fileName;
        this.componentsDir = componentsDir;
        this.subDir = subDir;
        this.fileContent = fileContent;

    }

    public String getFileName() {
        return fileName;
    }

    public String getComponentsDir() {
        return componentsDir;
    }

    public String getSubDir() {
        return subDir;
    }

    public JavaClassSource getFileContent() {
        return fileContent;
    }
}