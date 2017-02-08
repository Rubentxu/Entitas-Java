package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Test"})
public class Score implements IComponent {
    public String text;
    public int x;
    public int y;

    public Score(String text, int x, int y) {

        this.text = text;
        this.x = x;
        this.y = y;

    }

}
