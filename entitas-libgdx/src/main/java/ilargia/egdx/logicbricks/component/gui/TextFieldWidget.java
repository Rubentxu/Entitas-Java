package ilargia.egdx.logicbricks.component.gui;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Gui"})
public class TextFieldWidget implements IComponent {
    public TextField texField;


    public TextFieldWidget() {
    }

}
