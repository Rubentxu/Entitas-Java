package com.ilargia.games.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Game"})
public class TextureView implements IComponent {
    public String name;
    public TextureRegion texture;

    public TextureView(String name, TextureRegion texture, Body body) {
        this.name = name;
        this.texture = texture;
        this.body = body;

    }
}
