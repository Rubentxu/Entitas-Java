package com.ilargia.games.egdx.logicbricks.component.scene;

import com.badlogic.gdx.graphics.Color;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Scene"}, isSingleEntity = true)
public class Catch implements IComponent {
    public boolean catchBack = true;
    public boolean catchMenu = true;

}
