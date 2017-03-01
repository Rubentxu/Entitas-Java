package com.indignado.games.states.game.system;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.indignado.games.manager.SMGUIManager;
import com.indignado.games.states.game.component.input.PlayerInputController;
import com.indignado.games.states.game.gen.input.InputContext;
import com.indignado.games.states.game.gen.input.InputEntity;
import com.indignado.games.states.game.utils.GuiFactory;


public class InputsControllerSystem extends InputAdapter implements IInitializeSystem {
    private GuiFactory factory;
    private InputContext context;
    private InputEntity player;

    public InputsControllerSystem(InputContext context, GuiFactory factory) {
        this.context = context;
        this.factory = factory;

    }

    @Override
    public void initialize() {
        Gdx.input.setInputProcessor(this);
        player = context.getPlayerInputControllerEntity();
        if (context.isPadButtons()) {
            factory.createPadButtons(370 * SMGUIManager.ScaleUtil.getSizeRatio(), 190 * SMGUIManager.ScaleUtil.getSizeRatio(), player);
        } else if (context.isTouchPad()) {
            factory.createTouchPad(350 * SMGUIManager.ScaleUtil.getSizeRatio(), 350 * SMGUIManager.ScaleUtil.getSizeRatio(), player);
        }


    }


    @Override
    public boolean keyDown(int keycode) {
        PlayerInputController stateController = player.getPlayerInputController();
        switch (keycode) {
            case Input.Keys.LEFT:
                player.replacePlayerInputController(true, false, stateController.jumpPressed);
                break;
            case Input.Keys.RIGHT:
                player.replacePlayerInputController(false, true, stateController.jumpPressed);
                break;
            case Input.Keys.Z:
                player.replacePlayerInputController(stateController.leftPressed,
                        stateController.rightPressed, true);
                break;
            default:

        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        PlayerInputController stateController = player.getPlayerInputController();
        switch (keycode) {
            case Input.Keys.LEFT:
                player.replacePlayerInputController(false, false, stateController.jumpPressed);
                break;
            case Input.Keys.RIGHT:
                player.replacePlayerInputController(false, false, stateController.jumpPressed);
                break;
            case Input.Keys.Z:
                player.replacePlayerInputController(stateController.leftPressed,
                        stateController.rightPressed, true);
                break;
            default:

        }

        return true;
    }

}
