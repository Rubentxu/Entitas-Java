package com.indignado.games.states.game.system.actuator;


import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.factories.CollectionFactories;
import com.ilargia.games.entitas.factories.CollectionsFactory;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.indignado.games.states.game.component.actuator.CharacterActuator;
import com.indignado.games.states.game.component.game.Character;
import com.indignado.games.states.game.gen.*;
import com.indignado.games.states.game.gen.actuator.ActuatorContext;
import com.indignado.games.states.game.gen.actuator.ActuatorEntity;
import com.indignado.games.states.game.gen.actuator.ActuatorMatcher;
import com.indignado.games.states.game.gen.game.GameContext;
import com.indignado.games.states.game.gen.game.GameEntity;
import com.indignado.games.states.game.gen.game.GameMatcher;

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
        this.actuatorMap = CollectionFactories.createMap(String.class, CharacterActuator.class);

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


