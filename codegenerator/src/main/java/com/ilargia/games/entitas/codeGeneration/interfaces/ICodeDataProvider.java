package com.ilargia.games.entitas.codeGeneration.interfaces;

import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile;
import java.util.List;

public interface ICodeDataProvider extends ICodeGeneratorInterface {

    List<SourceDataFile> getData();
}
