package com.ilargia.games.egdx.base.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.ilargia.games.egdx.api.managers.PhysicsManager;
import com.ilargia.games.egdx.base.util.BodyBuilder;

public class BasePhysicsManager implements PhysicsManager<World,ContactListener> {
    private World physics;
    private BodyBuilder bodyBuilder;

    public BasePhysicsManager(Vector2 gravity) {
        physics = new World(gravity, false);
        bodyBuilder = new BodyBuilder(physics);

    }

    @Override
    public void initialize() {

    }

    @Override
    public void addListener(ContactListener listener) {
        physics.setContactListener(listener);
    }

    @Override
    public World getPhysics() {
        return physics;
    }

    @Override
    public BodyBuilder getBodyBuilder() {
        return bodyBuilder;
    }


    @Override
    public void dispose() {

    }

}
