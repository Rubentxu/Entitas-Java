package ilargia.entitas.codeGeneration.interfaces;

import java.util.List;
import java.util.Map;

public interface ICodeDataProvider<T extends Object, CodeData extends Map<String, T>> extends ICodeGeneratorInterface {

    List<CodeData> getData();
}
