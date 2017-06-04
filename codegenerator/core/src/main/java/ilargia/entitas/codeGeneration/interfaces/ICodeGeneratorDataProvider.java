package ilargia.entitas.codeGeneration.interfaces;

import ilargia.entitas.codeGeneration.data.CodeGeneratorData;

import java.util.List;

public interface ICodeGeneratorDataProvider extends ICodeGeneratorInterface {

    void setAppDomain(IAppDomain appDomain);

    List<CodeGeneratorData> getData();

}
