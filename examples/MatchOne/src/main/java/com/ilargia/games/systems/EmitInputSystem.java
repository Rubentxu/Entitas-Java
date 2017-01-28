package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.ContextExtensions;
import com.ilargia.games.GameBoardLogic;
import com.ilargia.games.components.GameBoard;
import com.ilargia.games.core.*;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;


public class EmitInputSystem implements IExecuteSystem, ICleanupSystem {

    private InputContext context;
    private Group<InputEntity> inputs;

    public EmitInputSystem(InputContext context) {
        this.context = context;
        this.inputs = context.getGroup(InputMatcher.Input());
    }


    @Override
    public void execute(float deltaTime) {
        if(Gdx.input.isButtonPressed(Input.Keys.B)) {
            context.setBurstMode(!context.isBurstMode());
        }

        boolean input = context.isBurstMode()
                ? Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                : Gdx.input.isButtonPressed(Input.Buttons.RIGHT);

        if(input) {
//
//            hit = Physics2D.Raycast(Camera.main.ScreenToWorldPoint(Input.mousePosition), Vector2.zero, 100);
//            if(hit.collider != null) {
//                Vector2 pos = hit.collider.transform.position;
//
//                context.createEntity()
//                        .addInput((int)pos.x, (int)pos.y);
//            }
        }
    }

    @Override
    public void cleanup() {
        for(InputEntity e : inputs.getEntities()) {
            context.destroyEntity(e);
        }
    }
}
