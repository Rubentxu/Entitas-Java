package ilargia.fixtures.components.game;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;
import ilargia.entitas.codeGenerator.annotations.Unique;

@Unique
@Contexts(names = {"Game"})
public class Ball implements IComponent {
    public boolean resetBall;

    public Ball() {
    }

    public Ball(boolean resetBall) {
        this.resetBall = resetBall;
    }
}