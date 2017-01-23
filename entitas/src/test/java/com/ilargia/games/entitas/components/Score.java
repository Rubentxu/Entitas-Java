package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.api.IComponent;

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
