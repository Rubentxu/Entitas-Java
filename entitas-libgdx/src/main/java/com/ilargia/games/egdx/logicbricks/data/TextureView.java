package com.ilargia.games.egdx.logicbricks.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureView {
    public String reference;
    public TextureRegion texture;
    public Bounds bounds;
    public boolean flipX = false;
    public boolean flipY = false;
    public int opacity = 1;
    public int layer = 0;
    public Color tint = Color.WHITE;

    public TextureView(String reference, Bounds bounds) {
       this(reference, null, bounds);
    }

    public TextureView(String reference, TextureRegion texture, Bounds bounds) {
        this.reference = reference;
        this.texture = texture;
        this.bounds = bounds;
    }
}
