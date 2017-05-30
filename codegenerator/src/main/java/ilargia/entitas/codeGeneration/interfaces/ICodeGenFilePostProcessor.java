package ilargia.entitas.codeGeneration.interfaces;

import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.SourceDataFile;

import java.util.List;

public interface ICodeGenFilePostProcessor extends ICodeGeneratorInterface {

    List<CodeGenFile> postProcess(List<CodeGenFile> files);
}
