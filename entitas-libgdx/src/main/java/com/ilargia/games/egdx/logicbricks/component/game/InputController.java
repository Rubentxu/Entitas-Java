package com.ilargia.games.egdx.logicbricks.component.game;

import com.ilargia.games.egdx.api.GameController;
import com.ilargia.games.egdx.impl.managers.InputManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Game"})
public class InputController implements IComponent {
    public GameController<InputManagerGDX, Entitas> controller;


    public InputController(GameController controller) {
        this.controller = controller;
    }
}
