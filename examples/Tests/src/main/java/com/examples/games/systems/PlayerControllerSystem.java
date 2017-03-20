package com.examples.games.systems;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.impl.managers.LogManagerGDX;
import com.ilargia.games.egdx.logicbricks.component.sensor.KeyboardSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;

public class PlayerControllerSystem implements IExecuteSystem {
    private final Entitas entitas;

    public PlayerControllerSystem(Entitas entitas) {
        this.entitas =  entitas;


    }

    @Override
    public void execute(float deltaTime) {
        GameEntity player = entitas.game.getPlayerEntity();
        for (SensorEntity sensorEntity : Indexed.getSensorsEntities(player)) {
            if(sensorEntity.getLink().pulse) {
                Body body = player.getRigidBody().body;
                KeyboardSensor sensor = sensorEntity.getKeyboardSensor();
                LogManagerGDX.debug("PlayerControllerSystem", "pulse %s Keycode", sensorEntity.getLink().pulse, sensor.keyCode);
                if(sensor.keyCode == Input.Keys.SPACE) {
                    body.applyForceToCenter(0, 30,true);
                }
            }

        }

    }


}
