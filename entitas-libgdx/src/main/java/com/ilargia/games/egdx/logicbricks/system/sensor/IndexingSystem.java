package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.system.IInitializeSystem;

public class IndexingSystem implements IInitializeSystem {
    private final Entitas entitas;
    private Indexed indexed;

    public IndexingSystem(Entitas entitas) {
        this.entitas = entitas;

    }

    @Override
    public void initialize() {
       indexed =  new Indexed(entitas);
    }

}
