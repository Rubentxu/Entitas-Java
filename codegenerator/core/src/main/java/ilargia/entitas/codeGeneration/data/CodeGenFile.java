package ilargia.entitas.codeGeneration.data;

import org.jboss.forge.roaster.model.source.JavaClassSource;

public class CodeGenFile {

    private String fileName;
    private String projectDir;
    private String subDir;
    private JavaClassSource fileContent;


    public CodeGenFile(String fileName, String projectDir, String subDir, JavaClassSource fileContent) {
        this.fileName = fileName;
        this.projectDir = projectDir;
        this.subDir = subDir;
        this.fileContent = fileContent;

    }

    public String getFileName() {
        return fileName;
    }

    public String getProjectDir() {
        return projectDir;
    }

    public String getSubDir() {
        return subDir;
    }

    public JavaClassSource getFileContent() {
        return fileContent;
    }
}