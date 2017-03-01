package com.indignado.games.states.game.system.actuator;


import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.factories.CollectionFactories;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.indignado.games.states.game.component.actuator.TextureActuator;
import com.indignado.games.states.game.component.game.TextureView;
import com.indignado.games.states.game.gen.*;
import com.indignado.games.states.game.gen.actuator.ActuatorContext;
import com.indignado.games.states.game.gen.actuator.ActuatorEntity;
import com.indignado.games.states.game.gen.actuator.ActuatorMatcher;
import com.indignado.games.states.game.gen.game.GameContext;
import com.indignado.games.states.game.gen.game.GameEntity;
import com.indignado.games.states.game.gen.game.GameMatcher;

import java.util.Map;


public class TextureActuatorSystem implements IInitializeSystem, IExecuteSystem {
    private final GameContext gameContext;
    private final ActuatorContext actuatorContext;
    private final Map<String, TextureActuator> actuatorMap;
    private Group<ActuatorEntity> textureActuatorGroup;
    private Group<GameEntity> textureViewGroup;

    public TextureActuatorSystem(Entitas entitas) {
        this.gameContext = entitas.game;
        this.actuatorContext = entitas.actuator;
        this.actuatorMap = CollectionFactories.createMap(String.class, TextureActuator.class);

    }

    @Override
    public void initialize() {
        textureActuatorGroup = actuatorContext.getGroup(ActuatorMatcher.TextureActuator());
        textureViewGroup = gameContext.getGroup(Matcher.AllOf(GameMatcher.TextureView()));

    }

    @Override
    public void execute(float deltaTime) {
        for (ActuatorEntity e : textureActuatorGroup.getEntities()) {
            TextureActuator textureActuator = e.getTextureActuator();
            actuatorMap.put(textureActuator.target, textureActuator);

        }

        String tag;
        for (GameEntity e : textureViewGroup.getEntities()) {
            TextureView view = e.getTextureView();
            tag = e.getCharacter().tag;
            if (actuatorMap.containsKey(tag)) {
                TextureActuator actuator = actuatorMap.get(tag);
                if (actuator.bounds != null) view.bounds = actuator.bounds;
                if (actuator.flipX != null) view.flipX = actuator.flipX;
                if (actuator.flipY != null) view.flipY = actuator.flipY;
                if (actuator.opacity != -1) view.opacity = actuator.opacity;
                if (actuator.tint != null) view.tint = actuator.tint;

            }
        }
        actuatorMap.clear();
        for (ActuatorEntity e : textureActuatorGroup.getEntities()) {
            e.destroy();
        }


    }

}


