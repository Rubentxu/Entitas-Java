package ilargia.egdx.logicbricks.component.gui;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Gui"})
public class Position implements IComponent {
    public float x;
    public float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

}
