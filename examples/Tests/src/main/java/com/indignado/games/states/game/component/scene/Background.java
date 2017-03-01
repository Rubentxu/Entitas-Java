package com.indignado.games.states.game.component.scene;

import com.badlogic.gdx.graphics.Texture;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Scene"}, isSingleEntity = true)
public class Background implements IComponent {
    public Texture front;
    public Texture middle;
    public Texture back;

}