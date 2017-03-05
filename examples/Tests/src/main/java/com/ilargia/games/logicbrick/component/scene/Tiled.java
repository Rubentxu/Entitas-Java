package com.ilargia.games.logicbrick.component.scene;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Scene"}, isSingleEntity = true)
public class Tiled implements IComponent {
    public String tileMapName;
    public float unitScale;

}