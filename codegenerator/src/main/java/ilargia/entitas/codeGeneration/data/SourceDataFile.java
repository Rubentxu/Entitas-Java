package ilargia.entitas.codeGeneration.data;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import java.util.HashMap;

public class SourceDataFile extends HashMap<String, Object> {

    public String fileName;
    public String componentsDir;
    public String subDir;
    public JavaClassSource source;


    public SourceDataFile(String fileName, String componentsDir, String subDir, JavaClassSource fileContent) {
        this.fileName = fileName;
        this.componentsDir = componentsDir;
        this.subDir = subDir;
        this.source = fileContent;

    }

}