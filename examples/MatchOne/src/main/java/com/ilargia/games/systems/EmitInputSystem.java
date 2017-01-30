package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.core.InputContext;
import com.ilargia.games.core.InputEntity;
import com.ilargia.games.core.InputMatcher;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;


public class EmitInputSystem implements IExecuteSystem, ICleanupSystem {

    private InputContext context;
    private Group<InputEntity> inputs;
    Body hitBody = null;
    Vector3 testPoint = new Vector3();
    OrthographicCamera cam;
    World world;

    public EmitInputSystem(InputContext context, World world, OrthographicCamera cam) {
        this.context = context;
        this.inputs = context.getGroup(InputMatcher.Input());
        this.world = world;
        this.cam = cam;

    }


    @Override
    public void execute(float deltaTime) {
        if (Gdx.input.isButtonPressed(Input.Keys.B)) {
            context.setBurstMode(!context.isBurstMode());
        }

        boolean input = context.isBurstMode()
                ? Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                : Gdx.input.isButtonPressed(Input.Buttons.RIGHT);

        if (input) {
            testPoint.set(Gdx.input.getX(), Gdx.input.getY(),0);
            cam.unproject(testPoint);

            hitBody = null;
            world.QueryAABB(callback, testPoint.x - 0.1f, testPoint.y - 0.1f, testPoint.x + 0.1f, testPoint.y + 0.1f);

            if (hitBody != null) {
                context.createEntity()
                        .addInput(hitBody.getPosition().x, hitBody.getPosition().y);
            }
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
        for (InputEntity e : inputs.getEntities()) {
            context.destroyEntity(e);
        }
    }

    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture (Fixture fixture) {
            // if the hit point is inside the fixture of the body
            // we report it
            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;
        }
    };
}
