package com.ilargia.games.core.component.gui;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.indignado.games.states.game.component.gui.Label;


@Component(pools = {"Gui"})
public class LabelWidget implements IComponent {
    public String text;
    public Label label;


    public LabelWidget(String text) {
        this.text = text;
    }

}
