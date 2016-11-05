package com.ilargia.games.entitas.codeGenerator.interfaces;


import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;

public interface IBlueprintsCodeGenerator extends ICodeGenerator {

    CodeGenFile[] generate(String[] blueprintNames);

}
