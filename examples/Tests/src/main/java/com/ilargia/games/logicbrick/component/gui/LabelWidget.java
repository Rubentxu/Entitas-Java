package com.ilargia.games.logicbrick.component.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;



@Component(pools = {"Gui"})
public class LabelWidget implements IComponent {
    public String text;
    public Label label;


    public LabelWidget(String text) {
        this.text = text;
    }

}
