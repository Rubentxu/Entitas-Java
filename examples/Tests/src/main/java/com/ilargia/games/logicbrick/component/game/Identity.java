package com.ilargia.games.logicbrick.component.game;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Game"})
public class Identity implements IComponent {
    public String type;
    public String tags;
}

