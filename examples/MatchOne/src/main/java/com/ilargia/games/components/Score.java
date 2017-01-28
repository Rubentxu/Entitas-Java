package com.ilargia.games.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Score"}, isSingleEntity = true)
public class Score implements IComponent {
    public int value;
}
