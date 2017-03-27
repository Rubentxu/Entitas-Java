package com.ilargia.games.egdx.logicbricks.data;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class RigidBody {
    public Map<String,Body> bodies;
    public Map<String, Joint> joints;
    public Map<String, Fixture> fixtures;

    public RigidBody() {
        if(this.bodies != null) {
            this.bodies.clear();
            this.fixtures.clear();
            this.joints.clear();
        } else {
            this.bodies = EntitasCollections.createMap(String.class, Body.class);
            this.fixtures = EntitasCollections.createMap(String.class, Fixture.class);
            this.joints = EntitasCollections.createMap(String.class, Joint.class);

        }

    }
}
