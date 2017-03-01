package com.ilargia.games.core.component.state;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"GameState"}, isSingleEntity = true)
public class Score implements IComponent {
    public int value;
}
