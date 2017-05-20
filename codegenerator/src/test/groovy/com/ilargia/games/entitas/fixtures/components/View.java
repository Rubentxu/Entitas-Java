package com.ilargia.games.entitas.fixtures.components;

import com.badlogic.gdx.math.Shape2D;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = { "Input"})
public class View implements IComponent {
    public Shape2D shape;

    public View(Shape2D shape) {
        this.shape = shape;
    }
}