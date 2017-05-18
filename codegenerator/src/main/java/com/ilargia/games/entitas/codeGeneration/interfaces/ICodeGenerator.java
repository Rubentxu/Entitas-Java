package com.ilargia.games.entitas.codeGeneration.interfaces;


import com.ilargia.games.entitas.codeGeneration.data.CodeGenFile;
import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile;
import java.util.List;

public interface ICodeGenerator extends ICodeGeneratorInterface {

    List<CodeGenFile> generate(List<SourceDataFile> data, String pkgDestiny);
}
