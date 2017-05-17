package com.ilargia.games.entitas.codeGeneration.interfaces;


import com.ilargia.games.entitas.codeGeneration.CodeGenFile;
import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;

import java.util.List;

public interface ICodeGenerator extends ICodeGeneratorInterface {

    List<CodeGenFile> generate(List<CodeGeneratorData> data);
}
