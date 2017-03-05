package com.ilargia.games.entitas.components;

import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Core"})
public class Bounds implements IComponent {
    public Rectangle rectangle;
    public Tag tag;
    public Bounds() {
    }

    public Bounds(float x, float y, float width, float height, Tag tag) {
        this.rectangle = new Rectangle(x, y, width, height);
        this.tag = tag;
    }

    public enum Tag {BoundPlayer1, BoundPlayer2}
}