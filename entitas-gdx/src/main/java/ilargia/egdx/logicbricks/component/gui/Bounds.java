package ilargia.egdx.logicbricks.component.gui;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Gui"})
public class Bounds implements IComponent {
    public float extentsX;
    public float extentsY;

    public Bounds(float extentsX, float extentsY) {
        this.extentsX = extentsX;
        this.extentsY = extentsY;
    }

}
