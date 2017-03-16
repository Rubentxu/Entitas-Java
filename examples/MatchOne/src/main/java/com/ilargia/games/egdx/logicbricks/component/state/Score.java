package com.ilargia.games.egdx.logicbricks.component.state;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.Component;


@Component(pools = {"GameState"}, isSingleEntity = true)
public class Score implements IComponent {
    public int value;
}
