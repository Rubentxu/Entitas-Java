package com.ilargia.games.logicbrick.component;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.Component;

@Component(pools = {"Core"})
public class Player implements IComponent {
    public ID id;

    public enum ID {PLAYER1, PLAYER2}


}