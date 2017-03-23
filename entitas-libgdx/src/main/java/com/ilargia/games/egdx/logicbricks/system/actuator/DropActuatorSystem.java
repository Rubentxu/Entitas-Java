package com.ilargia.games.egdx.logicbricks.system.actuator;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.impl.managers.InputManagerGDX;
import com.ilargia.games.egdx.impl.managers.LogManagerGDX;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.impl.managers.PreferencesManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorEntity;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.group.Group;

public class DropActuatorSystem implements IInitializeSystem, IExecuteSystem {
    private final Engine engine;
    private final Group<ActuatorEntity> group;
    private InputManagerGDX inputManager;
    private PreferencesManagerGDX preferences;
    private PhysicsManagerGDX physicsManager;

    private Body groundBody;
    private Body hitBody;
    private MouseJoint mouseJoint = null;


    public DropActuatorSystem(Entitas entitas, Engine engine) {
        this.engine = engine;
        group = entitas.actuator.getGroup(ActuatorMatcher.DropActuator());

    }

    @Override
    public void initialize() {
        this.inputManager = engine.getManager(InputManagerGDX.class);
        this.physicsManager = engine.getManager(PhysicsManagerGDX.class);
        this.preferences = engine.getManager(PreferencesManagerGDX.class);

        float halfWidth =  2f;
        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(new Vector2[] {
                new Vector2(-halfWidth, 0f),
                new Vector2(halfWidth, 0f),
                new Vector2(halfWidth, preferences.GAME_HEIGHT),
                new Vector2(-halfWidth, preferences.GAME_HEIGHT) });
        BodyDef chainBodyDef = new BodyDef();
        chainBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBody = physicsManager.getPhysics().createBody(chainBodyDef);
        groundBody.createFixture(chainShape, 0);
        chainShape.dispose();
    }


    @Override
    public void execute(float delatTime) {
        for (ActuatorEntity e : group.getEntities()) {
            e.getDropActuator();
            InputManagerGDX.TouchState touch = inputManager.getTouchState(0);
            if(inputManager.isTouchPressed(0)) {

                // ask the world which bodies are within the given
                // bounding box around the mouse pointer
                hitBody = null;
                physicsManager.getPhysics().QueryAABB(callback, touch.coordinates.x - 0.1f, touch.coordinates.y - 0.1f,
                        touch.coordinates.x + 0.1f, touch.coordinates.y + 0.1f);

                // if we hit something we create a new mouse joint
                // and attach it to the hit body.
                if (hitBody != null) {
                    GameEntity entity = Indexed.getInteractiveEntity((Integer) hitBody.getUserData());
                    LogManagerGDX.debug("DropActuatorSystem", "created MouseJoint %s", entity.getTags().values );

                    MouseJointDef def = new MouseJointDef();
                    def.bodyA = groundBody;
                    def.bodyB = hitBody;
                    def.collideConnected = true;
                    def.target.set(touch.coordinates.x, touch.coordinates.y);
                    def.maxForce = 1000.0f * hitBody.getMass();

                    mouseJoint = (MouseJoint) physicsManager.getPhysics().createJoint(def);
                    hitBody.setAwake(true);
                }

            } else if(inputManager.isTouchDown(0)) {

                // if a mouse joint exists we simply update
                // the target of the joint based on the new
                // mouse coordinates
                if (mouseJoint != null) {
                    LogManagerGDX.debug("DropActuatorSystem", "drop MouseJoint");
                    mouseJoint.setTarget(touch.displacement);
                }

            } else if(inputManager.isTouchReleased(0)){

                if (mouseJoint != null) {
                    LogManagerGDX.debug("DropActuatorSystem", "destroy MouseJoint");
                    physicsManager.getPhysics().destroyJoint(mouseJoint);
                    mouseJoint = null;
                }

            }
        }
    }


    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if (fixture.getBody() == groundBody)
                return true;

            if (fixture.testPoint(inputManager.getTouchState(0).coordinates.x, inputManager.getTouchState(0).coordinates.y)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;
        }
    };

}
