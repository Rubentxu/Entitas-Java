package com.ilargia.games.logicbrick.component.game;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.Component;


@Component(pools = {"Game"})
public class Asset implements IComponent {
    public String name;

    public Asset(String name) {
        this.name = name;
    }
}
