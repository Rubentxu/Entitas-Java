package com.indignado.games.states.game.component.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Game"})
public class RigidBody implements IComponent {
    public Body body;
}
