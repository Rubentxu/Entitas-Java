package ilargia.entitas.fixtures.components.dir;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"Game"})
public class Bounds implements IComponent {
    public float x, y, width, height;
    public Tag tag;

    public Bounds() {
    }

    public Bounds(float x, float y, float width, float height, Tag tag) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tag = tag;
    }

    public enum Tag {BoundPlayer1, BoundPlayer2}
}