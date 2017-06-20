package ilargia.fixtures.components.game;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"State"})
public class Score implements IComponent {
    public int value;

    public Score(int value) {
        this.value = value;
    }
}
