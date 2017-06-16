package ilargia.entitas.fixtures.components.test;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"Test"})
public class Motion implements IComponent {
    public float xVelocity;
    public float yVelocity;

    public Motion(float x, float y) {
        this.xVelocity = x;
        this.yVelocity = y;
    }


}
