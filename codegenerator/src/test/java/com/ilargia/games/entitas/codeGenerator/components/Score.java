package com.ilargia.games.entitas.codeGenerator.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Core"})
public class Score implements IComponent {
    public int value;

    public Score(int value) {
        this.value = value;
    }
}
