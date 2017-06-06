package ilargia.entitas.fixtures.components.test;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"Test"})
public class Size implements IComponent {
    public int width;
    public int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
}