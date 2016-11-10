package com.ilargia.games.entitas.codeGenerator.interfaces;


import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;

public interface IPoolCodeGenerator extends ICodeGenerator {
    public CodeGenFile[] generate(String[] poolNames, String pkgDestiny);

}
