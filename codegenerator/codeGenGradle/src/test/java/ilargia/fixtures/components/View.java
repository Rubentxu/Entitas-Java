package ilargia.fixtures.components;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;
import ilargia.entitas.codeGenerator.annotations.DontGenerate;

@DontGenerate
@Contexts(names = {"Input"})
public class View<T> implements IComponent {
    public T shape;

    public View(T shape) {
        this.shape = shape;
    }
}