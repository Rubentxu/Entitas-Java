package com.ilargia.games.core.component.gui;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Gui"})
public class Label implements IComponent {
    public String text;
    public String font;

    public Label(String text, String font) {
        this.text = text;
        this.font = font;
    }

}
