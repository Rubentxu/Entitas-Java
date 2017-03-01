package com.indignado.games.states.game.component.game;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Game"})
public class Movable implements IComponent {
    public float maxVelocity;
    public float jumpForce;
}
