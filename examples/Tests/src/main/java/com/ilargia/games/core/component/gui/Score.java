package com.ilargia.games.core.component.gui;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Gui"}, isSingleEntity = true)
public class Score implements IComponent {
    public int value;
}
