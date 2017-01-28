package com.ilargia.games.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Core"})
public class Asset implements IComponent {
    public String name;

    public Asset(String name) {
        this.name = name;
    }
}
