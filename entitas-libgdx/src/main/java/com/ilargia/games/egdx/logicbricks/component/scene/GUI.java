package com.ilargia.games.egdx.logicbricks.component.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Scene"}, isSingleEntity = true)
public class GUI implements IComponent {
    public Stage stage;

}