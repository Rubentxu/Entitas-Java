package com.ilargia.games.egdx.logicbricks.component.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Scene"}, isSingleEntity = true)
public class Camera implements IComponent {
    public OrthographicCamera camera;

}