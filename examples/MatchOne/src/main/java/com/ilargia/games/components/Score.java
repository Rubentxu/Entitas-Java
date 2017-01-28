package com.ilargia.games.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"GameSession"}, isSingleEntity = true)
public class Score implements IComponent {
    public int value;
}
