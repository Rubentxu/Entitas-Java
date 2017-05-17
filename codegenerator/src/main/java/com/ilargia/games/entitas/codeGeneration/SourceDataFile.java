package com.ilargia.games.entitas.codeGeneration;

import org.jboss.forge.roaster.model.source.JavaClassSource;

public class SourceDataFile extends CodeGeneratorData {

    public String fileName;
    public String projectDir;
    public String subDir;
    public JavaClassSource fileContent;


    public SourceDataFile(String fileName, String projectDir, String subDir, JavaClassSource fileContent) {
        this.fileName = fileName;
        this.projectDir = projectDir;
        this.subDir = subDir;
        this.fileContent = fileContent;

    }

}