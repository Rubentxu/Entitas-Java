package ilargia.entitas.codeGeneration.interfaces;

import ilargia.entitas.codeGeneration.data.SourceDataFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICodeDataProvider <T extends Object, CodeData extends Map<String, T>>extends ICodeGeneratorInterface {

    List<CodeData> getData();
}
