package ilargia.egdx.logicbricks.component.game;

import ilargia.egdx.logicbricks.data.StateCharacter;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Game"})
public class Character implements IComponent {
    public String tag;
    public StateCharacter currentState;
    public boolean facingLeft;

}
