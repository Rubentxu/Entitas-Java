package ilargia.entitas.fixtures.components;

import com.badlogic.gdx.math.Shape2D;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = { "Input"})
public class View implements IComponent {
    public Shape2D shape;

    public View(Shape2D shape) {
        this.shape = shape;
    }
}