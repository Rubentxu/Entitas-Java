package ilargia.entitas.codeGeneration.interfaces;


import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.SourceDataFile;

import java.util.List;

public interface ICodeGenerator extends ICodeGeneratorInterface {

    List<CodeGenFile> generate(List<SourceDataFile> data, String pkgDestiny);
}
