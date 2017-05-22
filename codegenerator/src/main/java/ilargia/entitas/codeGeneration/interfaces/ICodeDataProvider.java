package ilargia.entitas.codeGeneration.interfaces;

import ilargia.entitas.codeGeneration.data.SourceDataFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICodeDataProvider <CodeData extends Map<String, Object>>extends ICodeGeneratorInterface {

    List<CodeData> getData();
}
