package com.ilargia.games.entitas.codeGenerator.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Core", "Otro"})
public class Player implements IComponent {
    public ID id;

    ;

    public Player(ID id) {
        this.id = id;
    }


    public enum ID {PLAYER1, PLAYER2}


}