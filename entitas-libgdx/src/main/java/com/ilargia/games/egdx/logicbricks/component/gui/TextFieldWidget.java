package com.ilargia.games.egdx.logicbricks.component.gui;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Gui"})
public class TextFieldWidget implements IComponent {
    public TextField texField;


    public TextFieldWidget() {
    }

}
