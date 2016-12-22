package com.ilargia.games.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"})
public class TextureView implements IComponent {
    public final String name;
    public final Texture texture;
    public Vector2 position;
    public float rotation = 0;
    public final float height;
    public final float width;


    public TextureView(String name, Texture textureRegion, Vector2 position, float rotation, float height,
                       float width) {
        this.name = name;
        this.texture = textureRegion;
        this.position = position;
        this.rotation = rotation;
        this.height = height;
        this.width = width;

    }
}
