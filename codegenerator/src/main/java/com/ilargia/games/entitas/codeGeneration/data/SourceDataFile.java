package com.ilargia.games.entitas.codeGeneration.data;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import java.util.HashMap;

public class SourceDataFile extends HashMap<String, Object> {

    public String fileName;
    public String projectDir;
    public String subDir;
    public JavaClassSource source;


    public SourceDataFile(String fileName, String projectDir, String subDir, JavaClassSource fileContent) {
        this.fileName = fileName;
        this.projectDir = projectDir;
        this.subDir = subDir;
        this.source = fileContent;

    }

}