package com.ilargia.games.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"})
public class Score implements IComponent {
    public String text;
    public int x;
    public int y;
    public int points;

    public Score(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.points = 0;

    }

}
