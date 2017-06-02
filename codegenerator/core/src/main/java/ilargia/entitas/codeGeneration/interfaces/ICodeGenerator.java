package ilargia.entitas.codeGeneration.interfaces;


import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;

import java.util.List;

public interface ICodeGenerator<C> extends ICodeGeneratorInterface {

    List<CodeGenFile<C>> generate(List<CodeGeneratorData> data);
}
