package ilargia.entitas.fixtures.components;

import com.badlogic.gdx.math.Shape2D;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;
import ilargia.entitas.codeGenerator.annotations.DontGenerate;

@DontGenerate
@Contexts(names = { "Input", "Test"})
public class View <T> implements IComponent {
    public T shape;

    public View(T shape) {
        this.shape = shape;
    }
}