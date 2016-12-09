package com.ilargia.games.entitas.codeGenerator.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core","Otro"})
public class Player implements IComponent {
    public enum ID { PLAYER1, PLAYER2 };

    public ID id;



    public Player(ID id) {
        this.id = id;
    }


}