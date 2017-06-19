package ilargia.fixtures.components.game;


import ilargia.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"Game", "Core"})
public class Score {
    public int value;

    public Score(int value) {
        this.value = value;
    }
}
