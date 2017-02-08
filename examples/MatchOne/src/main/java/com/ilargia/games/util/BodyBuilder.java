package com.ilargia.games.util;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class BodyBuilder {
    private final World world;
    FixtureDefBuilder fixtureDefBuilder;
    private BodyDef bodyDef;
    private ArrayList<FixtureDef> fixtureDefs;
    private ArrayList<Object> fixtureUserDatas;
    private Vector2 position = new Vector2();
    private float angle;
    private MassData massData = new MassData();
    private boolean massSet;


    public BodyBuilder(World world) {
        this.world = world;
        this.fixtureDefBuilder = new FixtureDefBuilder();
        this.fixtureDefs = new ArrayList<FixtureDef>();
        this.fixtureUserDatas = new ArrayList<Object>();
        reset(true);

    }


    public FixtureDefBuilder fixtureDefBuilder() {
        return fixtureDefBuilder;

    }


    private void reset(boolean disposeShapes) {

        if (fixtureDefs != null && disposeShapes) {
            for (int i = 0; i < fixtureDefs.size(); i++) {
                FixtureDef fixtureDef = fixtureDefs.get(i);
                fixtureDef.shape.dispose();
            }
        }

        bodyDef = new BodyDef();
        fixtureDefs.clear();
        fixtureUserDatas.clear();
        angle = 0f;
        position.set(0f, 0f);
        massSet = false;

    }


    public BodyBuilder type(BodyDef.BodyType type) {
        bodyDef.type = type;
        return this;

    }

    public BodyBuilder bullet() {
        bodyDef.bullet = true;
        return this;

    }


    public BodyBuilder fixedRotation() {
        bodyDef.fixedRotation = true;
        return this;

    }


    public BodyBuilder linearDamping(float linearDamping) {
        bodyDef.linearDamping = linearDamping;
        return this;

    }


    public BodyBuilder angularDamping(float angularDamping) {
        bodyDef.angularDamping = angularDamping;
        return this;

    }


    public BodyBuilder fixture(FixtureDefBuilder fixtureDef) {
        return fixture(fixtureDef, null);

    }


    public BodyBuilder fixture(FixtureDefBuilder fixtureDef, Object fixtureUserData) {
        fixtureDefs.add(fixtureDef.build());
        fixtureUserDatas.add(fixtureUserData);
        return this;

    }


    public BodyBuilder fixture(FixtureDef fixtureDef) {
        return fixture(fixtureDef, null);

    }


    public BodyBuilder fixture(FixtureDef fixtureDef, Object fixtureUserData) {
        fixtureDefs.add(fixtureDef);
        fixtureUserDatas.add(fixtureUserData);
        return this;

    }


    public BodyBuilder fixtures(FixtureDef[] fixtureDefs) {
        return fixtures(fixtureDefs, null);

    }


    public BodyBuilder fixtures(FixtureDef[] fixtureDefs, Object[] fixtureUserDatas) {
        if (fixtureUserDatas != null && (fixtureDefs.length != fixtureUserDatas.length))
            throw new RuntimeException("length mismatch between fixtureDefs(" + fixtureDefs.length + ") and fixtureUserDatas(" + fixtureUserDatas.length + ")");

        for (int i = 0; i < fixtureDefs.length; i++) {
            this.fixtureDefs.add(fixtureDefs[i]);

            Object fixtureUserData = fixtureUserDatas != null ? fixtureUserDatas[i] : null;
            this.fixtureUserDatas.add(fixtureUserData);
        }

        return this;

    }


    public BodyBuilder mass(float mass) {
        this.massData.mass = mass;
        this.massSet = true;
        return this;

    }


    public BodyBuilder inertia(float intertia) {
        this.massData.I = intertia;
        this.massSet = true;
        return this;
    }


    public BodyBuilder position(float x, float y) {
        this.position.set(x, y);
        return this;

    }


    public BodyBuilder angle(float angle) {
        this.angle = angle;
        return this;

    }


    public Body build() {
        return build(true);

    }


    public Body build(boolean disposeShapes) {
        Body body = world.createBody(bodyDef);

        for (int i = 0; i < fixtureDefs.size(); i++) {
            FixtureDef fixtureDef = fixtureDefs.get(i);
            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(fixtureUserDatas.get(i));
        }

        if (massSet) {
            MassData bodyMassData = body.getMassData();

            // massData.center.changeCurrentState(position);
            massData.center.set(bodyMassData.center);
            // massData.I = bodyMassData.I;

            body.setMassData(massData);
        }

        body.setTransform(position, angle);

        reset(disposeShapes);
        return body;

    }
}
