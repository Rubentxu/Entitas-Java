package ilargia.egdx.logicbricks.component.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;



@Component(pools = {"Gui"})
public class LabelWidget implements IComponent {
    public String text;
    public Label label;


    public LabelWidget(String text) {
        this.text = text;
    }

}
