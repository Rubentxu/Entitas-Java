package com.examples.games.entities;

import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.EntityFactory;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;


public class Mariano implements EntityFactory<Entitas, GameEntity>{
    @Override
    public void loadAssets(Engine engine) {

    }

    @Override
    public GameEntity create(Engine engine, Entitas entitas) {
        return null;
    }
}
