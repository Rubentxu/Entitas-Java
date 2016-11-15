package com.ilargia.games.entitas.codeGenerator.interfaces;

import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;

import java.io.IOException;
import java.util.List;

public interface ICodeGeneratorDataProvider {

    List<ComponentInfo> componentInfos() throws IOException;

    // Expected behaviour:
    // - Make pool names distinct
    // - Sort pools by name
    // - Add CodeGenerator.DEFAULT_POOL_NAME if no custom pools are set
    List<String> poolNames();

    List<String> blueprintNames();
}
