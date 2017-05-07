package com.ilargia.games.egdx.logicbricks.system;


import com.ilargia.games.egdx.logicbricks.component.sensor.Link;
import com.ilargia.games.egdx.logicbricks.data.Axis2D;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.egdx.logicbricks.system.sensor.RadarSensorSystem;
import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RadarSensorSystemTest {
    Entitas entitas;
    private EntitasCollections collections;
    private RadarSensorSystem radarSensorSystem;
    private SensorEntity sensorEntity;
    private SensorEntity sensorEntity2;
    private GameEntity boss;
    private GameEntity groundEntity;
    private GameEntity playerEntity;
    private SensorEntity sensorEntity3;
    private SensorEntity sensorEntity4;

    public RadarSensorSystemTest() {
        collections = new EntitasCollections(new CollectionsFactories() {});
        entitas = new Entitas();
        this.radarSensorSystem = new RadarSensorSystem(entitas);
        Indexed.initialize(entitas);

        boss = entitas.game.createEntity()
                .addTags("Enemy","Boss")
                .setInteractive(true);

        groundEntity = entitas.game.createEntity()
                .addTags("Ground","Ground")
                .setInteractive(true);

        playerEntity = entitas.game.createEntity()
                .addTags("Player","Player1")
                .setInteractive(true);

        sensorEntity = entitas.sensor.createEntity()
                .addRadarSensor("Boss", Axis2D.Xnegative, 1,1)
                .addLink(playerEntity.getCreationIndex());

        sensorEntity2 = entitas.sensor.createEntity()
                .addRadarSensor("Ground", Axis2D.Xnegative, 1,1)
                .addLink(playerEntity.getCreationIndex());

        sensorEntity4 = entitas.sensor.createEntity()
                .addRadarSensor("Ground", Axis2D.Xnegative, 1,1)
                .addLink(boss.getCreationIndex());



    }


    @Test
    public void queryTrue() {
        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
        radarSensorSystem.execute( 0.5F);
        Link link = sensorEntity.getLink();

        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        assertEquals(2, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals( 1, Indexed.getEntitiesInSensor(sensorEntity).size());

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor",  false);
        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);
        sensorEntity3 = entitas.sensor.createEntity()
                .addRadarSensor("Ground", Axis2D.Xnegative, 1,1)
                .addLink(playerEntity.getCreationIndex());

        assertEquals(3, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals(0, Indexed.getEntitiesInSensor(sensorEntity).size());

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), groundEntity.getCreationIndex(), "RadarSensor",  true);
        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);
        assertEquals(3, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals(0, Indexed.getEntitiesInSensor(sensorEntity).size());

        radarSensorSystem.execute( 0.5F);
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
        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor",  false);
        radarSensorSystem.execute( 0.5f);
        Link link = sensorEntity.getLink();

        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor",  true);
        radarSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void queryTrueAndModeTrue() {
        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor",  true);
        sensorEntity.addMode(true);
        Link link = sensorEntity.getLink();

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
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
        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor",  true);
        sensorEntity.addMode(false);
        Link link = sensorEntity.getLink();

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
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
        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
        sensorEntity.addFrequency(1);
        Link link = sensorEntity.getLink();

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void frecuencyAndQueryFalse() {
        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
        sensorEntity.addFrequency(1);
        Link link = sensorEntity.getLink();

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor",  true);
        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }


    @Test
    public void frecuencyAndQueryTrueAndModeTrue() {
        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
        sensorEntity.addFrequency(1).addMode(true);
        Link link = sensorEntity.getLink();

        radarSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
        radarSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void frecuencyAndQueryFalseAndModeFalse() {
        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
        sensorEntity.addFrequency(1).addMode(false);
        Link link = sensorEntity.getLink();

        radarSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
        radarSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

    }

}
