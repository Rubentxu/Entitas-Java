package ilargia.egdx.logicbricks.component.gui;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Gui"}, isSingleEntity = true)
public class Score implements IComponent {
    public int value;
}
