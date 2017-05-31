package ilargia.entitas.fixtures.components.dir;

import com.badlogic.gdx.math.Rectangle;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"Game"})
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