package ilargia.entitas.components;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Test"})
public class Motion implements IComponent {
    public float x;
    public float y;

    public Motion(float x, float y) {
        this.x = x;
        this.y = y;
    }


}
