package com.ilargia.games.egdx.logicbricks.system.actuator;


import com.ilargia.games.egdx.logicbricks.component.actuator.CharacterActuator;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorContext;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorEntity;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.egdx.logicbricks.component.game.Character;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

import java.util.Map;


public class CharacterActuatorSystem implements IInitializeSystem, IExecuteSystem {
    private final GameContext gameContext;
    private final ActuatorContext actuatorContext;
    private final Map<String, CharacterActuator> actuatorMap;
    private Group<ActuatorEntity> characterActuatorGroup;
    private Group<GameEntity> rigidBodyGroup;

    public CharacterActuatorSystem(Entitas entitas) {
        this.gameContext = entitas.game;
        this.actuatorContext = entitas.actuator;
        this.actuatorMap = EntitasCollections.createMap(String.class, CharacterActuator.class);

    }

    @Override
    public void initialize() {
        characterActuatorGroup = actuatorContext.getGroup(ActuatorMatcher.CharacterActuator());
        rigidBodyGroup = gameContext.getGroup(Matcher.AllOf(GameMatcher.Character()));

    }

    @Override
    public void execute(float deltaTime) {
        for (ActuatorEntity e : characterActuatorGroup.getEntities()) {
            CharacterActuator characterActuator = e.getCharacterActuator();
            actuatorMap.put(characterActuator.target, characterActuator);

        }


        for (GameEntity e : rigidBodyGroup.getEntities()) {
            Character character = e.getCharacter();
            if (actuatorMap.containsKey(character.tag)) {
                CharacterActuator actuator = actuatorMap.get(character.tag);
                character.currentState = actuator.newState;
                character.facingLeft = actuator.facingLeft;

            }
        }
        actuatorMap.clear();
        for (ActuatorEntity e : characterActuatorGroup.getEntities()) {
            e.destroy();
        }

    }

}


