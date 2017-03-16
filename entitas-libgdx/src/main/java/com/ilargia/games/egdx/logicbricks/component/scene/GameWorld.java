package com.ilargia.games.egdx.logicbricks.component.scene;

import com.badlogic.gdx.graphics.Color;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Scene"}, isSingleEntity = true)
public class GameWorld implements IComponent {

    public float width;
    public float height;
    public float metresToPixels = 64;
    public float pixelsToMetres = 1.0f / metresToPixels;
    public boolean catchBack = true;
    public boolean catchMenu = true;
    public Color backGroundColor = Color.BLUE;


}
