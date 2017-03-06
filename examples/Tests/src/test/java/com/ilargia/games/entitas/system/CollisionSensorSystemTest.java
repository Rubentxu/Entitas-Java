package com.ilargia.games.entitas.system;


import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.logicbrick.component.sensor.Signal;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.system.sensor.DelaySensorSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollisionSensorSystemTest {
    private EntitasCollections collections;
    private DelaySensorSystem sensorSystem;
    SensorEntity entity;


    @Before
    public void setUp() {
        collections = new EntitasCollections(new CollectionsFactories(){});
        Entitas entitas = new Entitas();
        this.sensorSystem = new DelaySensorSystem(entitas.sensor);
        entity = entitas.sensor.createEntity();
        entity.addSignal(null).addDelaySensor(0.5f, 1,false);

    }

    @Test
    public void defaultTest() {
        sensorSystem.execute(0.2F);
        Signal signal = entity.getSignal();

        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertTrue(signal.isOpen);
        assertTrue(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertTrue(signal.isChanged);

    }

    @Test
    public void modeTrue() {
        entity.addMode(true);
        Signal signal = entity.getSignal();

        sensorSystem.execute(0.2F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertTrue(signal.isOpen);
        assertTrue(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertTrue(signal.isChanged);


        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

    }

    @Test
    public void ModeFalse() {
        entity.addMode(false);
        Signal signal = entity.getSignal();

        sensorSystem.execute(0.2F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertTrue(signal.isOpen);
        assertTrue(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertTrue(signal.isChanged);


        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

    }

    @Test
    public void frecuency() {
        entity.addFrequency(1);
        Signal signal = entity.getSignal();

        sensorSystem.execute(0.2F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5f);
        assertTrue(signal.pulse);
        assertFalse(signal.isOpen);
        assertTrue(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertTrue(signal.isChanged);


        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5f);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

    }


    @Test
    public void frecuencyAndModeTrue() {

        entity.addFrequency(1).addMode(true);
        Signal signal = entity.getSignal();

        sensorSystem.execute(0.2f);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertFalse(signal.isOpen);
        assertTrue(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertTrue(signal.isChanged);


        sensorSystem.execute(0.5f);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

    }

    @Test
    public void frecuencyAndModeFalse() {

        entity.addFrequency(1).addMode(false);
        Signal signal = entity.getSignal();

        sensorSystem.execute(0.2f);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertFalse(signal.isOpen);
        assertTrue(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertTrue(signal.isChanged);


        sensorSystem.execute(0.5f);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertTrue(signal.isOpen);
        assertFalse(signal.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(signal.pulse);
        assertFalse(signal.isOpen);
        assertFalse(signal.isChanged);

    }


}
