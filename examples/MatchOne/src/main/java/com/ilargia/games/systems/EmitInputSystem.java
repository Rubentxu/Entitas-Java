package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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


public class EmitInputSystem extends InputAdapter {

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
        Gdx.input.setInputProcessor(this);
    }





    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture (Fixture fixture) {
            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;
        }
    };

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {

        boolean input = context.isBurstMode()
                ? button == Input.Buttons.LEFT
                :  button == Input.Buttons.RIGHT;

        if (true) {
            testPoint.set(x, y, 0);
            cam.unproject(testPoint);
            hitBody = null;
            world.QueryAABB(callback, testPoint.x , testPoint.y , testPoint.x , testPoint.y );

            if (hitBody != null) {
                Gdx.app.log("Input", hitBody.getPosition().toString());
                Gdx.app.log("Input",String.format("int X: %d int Y: %d", (int)hitBody.getPosition().x, (int)hitBody.getPosition().y));
                context.createEntity()
                        .addInput((int)hitBody.getPosition().x,(int) hitBody.getPosition().y);
            }
        }
        return false;
    }
}
