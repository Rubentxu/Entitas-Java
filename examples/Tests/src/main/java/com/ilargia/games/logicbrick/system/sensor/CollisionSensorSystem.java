package com.ilargia.games.logicbrick.system.sensor;


import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.logicbrick.gen.sensor.SensorContext;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorMatcher;

import java.util.List;

public class CollisionSensorSystem extends SensorSystem implements IExecuteSystem {
    private Group<SensorEntity> sensorGroup;

    public CollisionSensorSystem(SensorContext context) {
        sensorGroup = context.getGroup(Matcher.AllOf(SensorMatcher.CollisionSensor(), SensorMatcher.Link()));

    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        for (Contact contact : sensorEntity.getCollisionSensor().contactList) {
            if (contact.isTouching()) return true;
        }
        return false;

    }

    @Override
    public void execute(float deltaTime) {
        process(sensorGroup.getEntities(), deltaTime);

    }

}

