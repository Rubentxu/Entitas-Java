package com.ilargia.games.entitas.fixtures.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"Input", "Core"})
public class Player implements IComponent {
    public ID id;

    public Player(ID id) {
        this.id = id;
    }


    public enum ID {PLAYER1, PLAYER2}


}