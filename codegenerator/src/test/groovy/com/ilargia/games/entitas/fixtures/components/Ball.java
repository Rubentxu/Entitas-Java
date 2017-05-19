package com.ilargia.games.entitas.fixtures.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.codeGenerator.annotations.Contexts;
import com.ilargia.games.entitas.codeGenerator.annotations.Unique;

@Unique
@Contexts(name = {"Core"})
public class Ball implements IComponent {
    public boolean resetBall;

    public Ball() {
    }

    public Ball(boolean resetBall) {
        this.resetBall = resetBall;
    }
}