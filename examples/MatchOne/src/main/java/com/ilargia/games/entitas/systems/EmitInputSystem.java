package com.ilargia.games.entitas.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.ilargia.games.logicbrick.gen.input.InputEntity;
import com.ilargia.games.logicbrick.gen.input.InputMatcher;
import com.ilargia.games.logicbrick.gen.input.InputContext;
import com.ilargia.games.entitas.group.Group;


public class EmitInputSystem extends InputAdapter {

    Body hitBody = null;
    Vector3 testPoint = new Vector3();
    Camera cam;
    World world;
    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;
        }
    };
    private InputContext context;
    private Group<InputEntity> inputs;


    public EmitInputSystem(InputContext context, World world, Camera cam) {
        this.context = context;
        this.inputs = context.getGroup(InputMatcher.Input());
        this.world = world;
        this.cam = cam;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        boolean input = context.isBurstMode()
                ? button == Input.Buttons.LEFT
                : button == Input.Buttons.RIGHT;

        if (true) {
            testPoint.set(x, y, 0);
            cam.unproject(testPoint);
            hitBody = null;
            world.QueryAABB(callback, testPoint.x, testPoint.y, testPoint.x, testPoint.y);

            if (hitBody != null) {
                Gdx.app.log("Input", hitBody.getPosition().toString());
                Gdx.app.log("Input", String.format("int X: %d int Y: %d", (int) hitBody.getPosition().x, (int) hitBody.getPosition().y));
                context.createEntity()
                        .addInput((int) hitBody.getPosition().x, (int) hitBody.getPosition().y);
            }
        }
        return false;
    }
}
