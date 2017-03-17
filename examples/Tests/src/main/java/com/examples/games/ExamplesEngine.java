package com.examples.games;

import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.factories.CollectionsFactories;

public class ExamplesEngine extends EngineGDX {

    public ExamplesEngine() {
        super(new CollectionsFactories() {});

    }

}
