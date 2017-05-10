package com.ilargia.games.entitas.codeGeneration.interfaces;

import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;

import java.util.List;

public interface ICodeGeneratorDataProvider extends ICodeGeneratorInterface {

    List<CodeGeneratorData> getData();
}
