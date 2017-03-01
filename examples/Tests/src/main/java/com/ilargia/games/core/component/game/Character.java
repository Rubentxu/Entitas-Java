package com.ilargia.games.core.component.game;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.core.data.StateCharacter;

@Component(pools = {"Game"})
public class Character implements IComponent {
    public String tag;
    public StateCharacter currentState;
    public boolean facingLeft;

}
