package com.ilargia.games.entitas.codeGenerator.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core","Otro"}, isSingleEntity = true)
public class Ball implements IComponent {
    public Ball() {
    }

    public boolean resetBall;

    public Ball(boolean resetBall) {
        this.resetBall = resetBall;
    }
}