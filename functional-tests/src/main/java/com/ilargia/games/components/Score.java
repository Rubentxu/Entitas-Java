package com.ilargia.games.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"})
public class Score implements IComponent {
    public int value;

    public Score(int value) {
        this.value = value;
    }

    public Score() {

    }
}
