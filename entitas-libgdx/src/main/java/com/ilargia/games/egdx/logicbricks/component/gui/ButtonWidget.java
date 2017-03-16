package com.ilargia.games.egdx.logicbricks.component.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Gui"})
public class ButtonWidget implements IComponent {
    public enum Type { Image, ImageText, Text, CheckBox}
    public Button button;
    public Type type;

    public ButtonWidget(Type type) {
        this.type = type;
    }

}
