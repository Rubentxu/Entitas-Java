package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.intermediate.ComponentInfo;

import java.util.List;
import java.util.Set;

public interface ICodeGeneratorDataProvider {

    List<ComponentInfo> componentInfos();

    // Expected behaviour:
    // - Make pool names distinct
    // - Sort pools by name
    // - Add CodeGenerator.DEFAULT_POOL_NAME if no custom pools are set
    Set<String> poolNames();

    List<String> blueprintNames();
}
