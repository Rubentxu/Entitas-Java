package com.ilargia.games.entitas.system;


import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;

import com.ilargia.games.logicbrick.component.sensor.Link;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.system.sensor.DelaySensorSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DelaySensorSystemTest {
    private EntitasCollections collections;
    private DelaySensorSystem sensorSystem;
    SensorEntity entity;


    @Before
    public void setUp() {
        collections = new EntitasCollections(new CollectionsFactories(){});
        Entitas entitas = new Entitas();
        this.sensorSystem = new DelaySensorSystem(entitas.sensor);
        entity = entitas.sensor.createEntity();
        entity.addLink(0).addCollisionSensor("Enemy");

    }

    @Test
    public void defaultTest() {
        sensorSystem.execute(0.2F);
        Link link = entity.getLink();

        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

    }

    @Test
    public void modeTrue() {
        entity.addMode(true);
        Link link = entity.getLink();

        sensorSystem.execute(0.2F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);


        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void ModeFalse() {
        entity.addMode(false);
        Link link = entity.getLink();

        sensorSystem.execute(0.2F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);


        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void frecuency() {
        entity.addFrequency(1);
        Link link = entity.getLink();

        sensorSystem.execute(0.2F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);


        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }


    @Test
    public void frecuencyAndModeTrue() {

        entity.addFrequency(1).addMode(true);
        Link link = entity.getLink();

        sensorSystem.execute(0.2f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);


        sensorSystem.execute(0.5f);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void frecuencyAndModeFalse() {

        entity.addFrequency(1).addMode(false);
        Link link = entity.getLink();

        sensorSystem.execute(0.2f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);


        sensorSystem.execute(0.5f);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }


}
