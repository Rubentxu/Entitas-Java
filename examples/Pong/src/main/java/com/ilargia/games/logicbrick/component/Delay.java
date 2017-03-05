package com.ilargia.games.logicbrick.component;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.Component;

@Component(pools = {"Core"})
public class Delay implements IComponent {
    public float duration;
    public float time;

    public Delay(float duration) {
        this.duration = duration;
        this.time = 0;
    }
}