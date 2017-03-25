package com.ilargia.games.egdx.impl.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.ilargia.games.egdx.api.managers.PhysicsManager;
import com.ilargia.games.egdx.api.managers.listener.Collision;
import com.ilargia.games.egdx.util.BodyBuilder;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Set;

public class PhysicsManagerGDX implements PhysicsManager<World, Collision>, ContactListener {
    private World physics;
    private BodyBuilder bodyBuilder;
    private Set<Collision> collisionListeners;

    public PhysicsManagerGDX(Vector2 gravity) {
        physics = new World(gravity, false);
        bodyBuilder = new BodyBuilder(physics);
        collisionListeners = EntitasCollections.createSet(Collision.class);

    }

    @Override
    public void initialize() {
        physics.setContactListener(this);
    }

    @Override
    public void addListener(Collision listener) {
        collisionListeners.add(listener);
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

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()) {
            String tagSensorA = (String) contact.getFixtureA().getUserData();
            String tagSensorB = (String) contact.getFixtureB().getUserData();
            Integer dataA = (Integer) contact.getFixtureA().getBody().getUserData();
            Integer dataB = (Integer) contact.getFixtureB().getBody().getUserData();
            for (Collision listener : collisionListeners) {
                listener.processSensorCollision(dataA, dataB, tagSensorA, true);
                listener.processSensorCollision(dataB, dataA, tagSensorB, true);

            }
        }
        Integer dataA = (Integer) contact.getFixtureA().getBody().getUserData();
        Integer dataB = (Integer) contact.getFixtureB().getBody().getUserData();
        for (Collision listener : collisionListeners) {
            listener.processCollision(dataA, dataB, true);
            listener.processCollision(dataB, dataA, true);
        }

    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()) {
            String tagSensorA = (String) contact.getFixtureA().getUserData();
            String tagSensorB = (String) contact.getFixtureB().getUserData();
            Integer dataA = (Integer) contact.getFixtureA().getBody().getUserData();
            Integer dataB = (Integer) contact.getFixtureB().getBody().getUserData();
            for (Collision listener : collisionListeners) {
                listener.processSensorCollision(dataA, dataB, tagSensorA, false);
                listener.processSensorCollision(dataB, dataA, tagSensorB, false);

            }
        }
            Integer dataA = (Integer) contact.getFixtureA().getBody().getUserData();
            Integer dataB = (Integer) contact.getFixtureB().getBody().getUserData();
            for (Collision listener : collisionListeners) {
                listener.processCollision(dataA, dataB, false);
                listener.processCollision(dataB, dataA, false);
            }


    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }

}
