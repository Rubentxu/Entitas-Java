package ilargia.entitas.codeGeneration.interfaces;

import ilargia.entitas.codeGeneration.data.SourceDataFile;

import java.util.List;

public interface ICodeDataProvider extends ICodeGeneratorInterface {

    List<SourceDataFile> getData();
}
