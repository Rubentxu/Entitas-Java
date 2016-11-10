package com.ilargia.games.entitas.codeGenerator.interfaces;


import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;

public interface IComponentCodeGenerator extends ICodeGenerator {
    public CodeGenFile[] generate(ComponentInfo[] componentInfos);

}
