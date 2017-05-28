package ilargia.entitas.fixtures.components.dir;

import ilargia.entitas.api.IComponent;

public class Size implements IComponent {
    public int width;
    public int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
}