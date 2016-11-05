package com.ilargia.games.entitas.codeGenerator.intermediate;

public class CodeGenFile {

    public String fileName;
    public String fileContent;
    public String generatorName;

    public CodeGenFile(String fileName, String fileContent, String generatorName) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.generatorName = generatorName;

    }

}