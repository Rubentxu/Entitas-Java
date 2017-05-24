package ilargia.egdx.logicbricks.component.game;

import ilargia.egdx.api.GameController;
import ilargia.egdx.impl.managers.InputManagerGDX;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Game"})
public class InputController implements IComponent {
    public GameController<InputManagerGDX, Entitas> controller;

    public InputController(GameController controller) {
        this.controller = controller;
    }
}
