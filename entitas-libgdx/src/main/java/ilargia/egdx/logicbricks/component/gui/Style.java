package ilargia.egdx.logicbricks.component.gui;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Gui"})
public class Style implements IComponent {
    public String name;

    public Style(String styleName) {
        this.name = styleName;
    }

}
