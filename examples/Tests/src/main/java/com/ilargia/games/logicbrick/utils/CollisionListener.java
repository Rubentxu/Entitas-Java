package com.ilargia.games.logicbrick.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ilargia.games.logicbrick.component.sensor.CollisionSensor;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;


public class CollisionListener implements ContactListener {

    private void process(GameEntity entityA, GameEntity entityB) {
      //

    }

    @Override
    public void beginContact(Contact contact) {
        Object dataA = contact.getFixtureA().getBody().getUserData();
        Object dataB = contact.getFixtureB().getBody().getUserData();

        if(dataA != null && dataB !=null) {

        }

        process((SensorEntity) , contact);
        process((SensorEntity) , contact);

    }


    @Override
    public void endContact(Contact contact) {
        process((SensorEntity) contact.getFixtureA().getBody().getUserData(), contact);
        process((SensorEntity) contact.getFixtureB().getBody().getUserData(), contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
