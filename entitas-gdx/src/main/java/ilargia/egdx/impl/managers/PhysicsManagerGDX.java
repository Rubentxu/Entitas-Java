package ilargia.egdx.impl.managers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ilargia.egdx.api.managers.PhysicsManager;
import ilargia.egdx.api.managers.listener.Collision;
import ilargia.egdx.util.BodyBuilder;
import ilargia.entitas.factories.EntitasCollections;

import java.util.Set;

public class PhysicsManagerGDX implements PhysicsManager<World, Collision<Fixture>>, ContactListener {
    private World physics;
    private BodyBuilder bodyBuilder;
    private Set<Collision<Fixture>> collisionListeners;

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
    public void addListener(Collision<Fixture> listener) {
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
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        for (Collision<Fixture> listener : collisionListeners) {
            listener.processCollision(fixtureA, fixtureB, true);
            listener.processCollision(fixtureB, fixtureA, true);
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        for (Collision<Fixture> listener : collisionListeners) {
            listener.processCollision(fixtureA, fixtureB, false);
            listener.processCollision(fixtureB, fixtureA, false);
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }

}
