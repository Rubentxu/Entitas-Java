package com.ilargia.games.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Core"})
public class Player implements IComponent {
    public ID id;

    public enum ID {PLAYER1, PLAYER2}


}