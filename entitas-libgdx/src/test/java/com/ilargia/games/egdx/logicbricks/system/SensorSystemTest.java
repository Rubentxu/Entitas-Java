package com.ilargia.games.egdx.logicbricks.system;


import com.ilargia.games.egdx.logicbricks.component.sensor.Link;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.system.sensor.SensorSystem;
import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SensorSystemTest {
    private EntitasCollections collections;
    private boolean query;
    private SensorSystem sensorSystem;
    SensorEntity entity;


    @Before
    public void setUp() {
        collections = new EntitasCollections(new CollectionsFactories(){});
        this.sensorSystem = new SensorSystem() {
            @Override
            public void execute(float deltaTime) {

            }

            @Override
            protected boolean query(SensorEntity sensor, float deltaTime) {
                return query;

            }
        };
        Entitas entitas = new Entitas();
        entity = entitas.sensor.createEntity();

        entity.addLink(0);
      

    }


    /* Pulses
    * :...
    * ------------
    * */
    @Test
    public void queryTrue() {
        query = true;
        sensorSystem.process(entity, 0.5F);
        Link link = entity.getLink();

        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        query = false;
        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);


    }

    /* Pulses
    * ....
    * ------------
    * */
    @Test
    public void queryFalse() {
        query = false;
        sensorSystem.process(entity, 0.5f);
        Link link = entity.getLink();

        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        query = true;
        sensorSystem.process(entity, 0.5f);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void queryTrueAndModeTrue() {
        query = true;
        entity.addMode(true);
        Link link = entity.getLink();

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        query = false;
        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    /* Pulses
    * ....
    * ------------
    * */
    @Test
    public void queryFalseAndModeFalse() {
        query = true;
        entity.addMode(false);
        Link link = entity.getLink();

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        query = false;
        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

    }

    /* Pulses
    * .:......
    * ------------
    * */
    @Test
    public void frecuencyAndQueryTrue() {
        query = true;
        entity.addFrequency(1);
        Link link = entity.getLink();

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5f);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        query = false;
        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5f);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void frecuencyAndQueryFalse() {
        query = false;
        entity.addFrequency(1);
        Link link = entity.getLink();

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        query = true;
        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5f);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }


    @Test
    public void frecuencyAndQueryTrueAndModeTrue() {
        query = true;
        entity.addFrequency(1).addMode(true);
        Link link = entity.getLink();

        sensorSystem.process(entity, 0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        query = false;
        sensorSystem.process(entity, 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void frecuencyAndQueryFalseAndModeFalse() {
        query = true;
        entity.addFrequency(1).addMode(false);
        Link link = entity.getLink();

        sensorSystem.process(entity, 0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        query = false;
        sensorSystem.process(entity, 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        sensorSystem.process(entity, 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

    }

}
