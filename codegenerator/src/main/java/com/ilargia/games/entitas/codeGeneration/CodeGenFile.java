package com.ilargia.games.entitas.codeGeneration;

import org.jboss.forge.roaster.model.source.JavaClassSource;

public class CodeGenFile {

    public String fileName;
    public JavaClassSource fileContent;
    public String generatorName;

    public CodeGenFile(String fileName, JavaClassSource fileContent, String generatorName) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.generatorName = generatorName;

    }

}