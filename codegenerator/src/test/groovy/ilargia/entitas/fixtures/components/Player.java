package ilargia.entitas.fixtures.components;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"Input", "Core"})
public class Player implements IComponent {
    public ID id;

    public Player(ID id) {
        this.id = id;
    }


    public enum ID {PLAYER1, PLAYER2}


}