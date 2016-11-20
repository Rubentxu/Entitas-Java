package com.ilargia.games.components;

import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"})
public class Player implements IComponent {
    public enum ID { PLAYER1, PLAYER2 };

    public ID id;

    public Player(ID id) {
        this.id = id;
    }


}