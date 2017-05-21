package com.ilargia.games.entitas.fixtures.components;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"Game", "Core"})
public class Score  {
    public int value;

    public Score(int value) {
        this.value = value;
    }
}
