package ilargia.egdx.logicbricks.component.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Gui"})
public class ImageWidget implements IComponent {
    public Image image;


    public ImageWidget() {
    }

}
