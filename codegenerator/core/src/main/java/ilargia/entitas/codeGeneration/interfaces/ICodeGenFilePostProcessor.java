package ilargia.entitas.codeGeneration.interfaces;

import ilargia.entitas.codeGeneration.data.CodeGenFile;

import java.util.List;

public interface ICodeGenFilePostProcessor<C> extends ICodeGeneratorInterface {

    void setAppDomain(IAppDomain appDomain);

    List<CodeGenFile<C>> postProcess(List<CodeGenFile<C>> files);
}
