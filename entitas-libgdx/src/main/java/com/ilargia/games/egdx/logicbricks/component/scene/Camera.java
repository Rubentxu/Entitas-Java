package com.ilargia.games.egdx.logicbricks.component.scene;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Scene"}, isSingleEntity = true)
public class Camera implements IComponent {
    public com.badlogic.gdx.graphics.Camera camera;

}