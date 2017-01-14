package com.ilargia.games.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"})
public class Delay implements IComponent {
    public final float duration;
    public float time;

    public Delay(float duration) {
        this.duration = duration;
        this.time = 0;
    }
}