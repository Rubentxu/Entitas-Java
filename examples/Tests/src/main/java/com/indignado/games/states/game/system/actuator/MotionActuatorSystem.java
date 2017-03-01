package com.indignado.games.states.game.system.actuator;


import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.factories.CollectionFactories;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.indignado.games.states.game.component.actuator.VelocityActuator;
import com.indignado.games.states.game.component.game.RigidBody;
import com.indignado.games.states.game.gen.Entitas;
import com.indignado.games.states.game.gen.actuator.ActuatorContext;
import com.indignado.games.states.game.gen.actuator.ActuatorEntity;
import com.indignado.games.states.game.gen.actuator.ActuatorMatcher;
import com.indignado.games.states.game.gen.game.GameContext;
import com.indignado.games.states.game.gen.game.GameEntity;
import com.indignado.games.states.game.gen.game.GameMatcher;

import java.util.Map;


public class MotionActuatorSystem implements IInitializeSystem, IExecuteSystem {
    private final GameContext gameContext;
    private final ActuatorContext actuatorContext;
    private final Map<String, VelocityActuator> actuatorMap;
    private Group<ActuatorEntity> velocityActuatorGroup;
    private Group<GameEntity> rigidBodyGroup;

    public MotionActuatorSystem(Entitas entitas) {
        this.gameContext = entitas.game;
        this.actuatorContext = entitas.actuator;
        this.actuatorMap = CollectionFactories.createMap(String.class, VelocityActuator.class);

    }

    @Override
    public void initialize() {
        velocityActuatorGroup = actuatorContext.getGroup(ActuatorMatcher.VelocityActuator());
        rigidBodyGroup = gameContext.getGroup(Matcher.AllOf(GameMatcher.RigidBody(), GameMatcher.Character()));

    }

    @Override
    public void execute(float deltaTime) {
        for (ActuatorEntity e : velocityActuatorGroup.getEntities()) {
            VelocityActuator velocityActuator = e.getVelocityActuator();
            actuatorMap.put(velocityActuator.target, velocityActuator);

        }

        String tag;
        for (GameEntity e : rigidBodyGroup.getEntities()) {
            RigidBody rigidBody = e.getRigidBody();
            tag = e.getCharacter().tag;
            if (actuatorMap.containsKey(tag)) {
                VelocityActuator actuator = actuatorMap.get(tag);
                rigidBody.body.setLinearVelocity(actuator.velocity);
                rigidBody.body.setAngularVelocity(actuator.angularVelocity);
            }
        }
        actuatorMap.clear();
        for (ActuatorEntity e : velocityActuatorGroup.getEntities()) {
            e.destroy();
        }

    }

}


