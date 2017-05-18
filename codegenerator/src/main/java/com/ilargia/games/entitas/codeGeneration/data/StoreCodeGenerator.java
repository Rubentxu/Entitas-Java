package com.ilargia.games.entitas.codeGeneration.data;


import java.util.HashMap;
import java.util.Map;

public class StoreCodeGenerator {
    public Map<String, CodeGenFile> codeGenFiles;
    public Map<String, CodeGenFile> sourceFiles;

    public StoreCodeGenerator() {
        this.codeGenFiles = new HashMap<>();
        this.sourceFiles = new HashMap<>();
    }
}
