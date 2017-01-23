package com.ilargia.games.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.api.IComponent;

@Component(pools = {"Core"})
public class TextureView implements IComponent {
    public final String name;
    public final TextureRegion texture;
    public final int height;
    public final int width;
    public Vector2 position;
    public float rotation = 0;


    public TextureView(String name, TextureRegion texture, Vector2 position, float rotation, int height, int width) {
        this.name = name;
        this.texture = texture;
        this.position = position;
        this.rotation = rotation;
        this.height = height;
        this.width = width;

    }
}
