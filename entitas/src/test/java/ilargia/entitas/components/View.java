package ilargia.entitas.components;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Test"})
public class View implements IComponent {
    public int shape;

    public View(int shape) {
        this.shape = shape;
    }
}