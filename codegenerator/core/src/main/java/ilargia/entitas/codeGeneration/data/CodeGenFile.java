package ilargia.entitas.codeGeneration.data;

public class CodeGenFile<C> {
    private String fileName;
    private C fileContent;
    private String subDir;

    public CodeGenFile(String fileName, C fileContent, String subDir ) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.subDir = subDir;

    }

    public String getFileName() {
        return fileName;
    }

    public String getSubDir() {
        return subDir;
    }

    public C getFileContent() {
        return fileContent;
    }
}