package ilargia.entitas.codeGeneration.data;

import org.jboss.forge.roaster.model.source.JavaClassSource;

public class CodeGenFile {

    public String fileName;
    public String projectDir;
    public String subDir;
    public JavaClassSource fileContent;


    public CodeGenFile(String fileName, String projectDir, String subDir, JavaClassSource fileContent) {
        this.fileName = fileName;
        this.projectDir = projectDir;
        this.subDir = subDir;
        this.fileContent = fileContent;

    }

}