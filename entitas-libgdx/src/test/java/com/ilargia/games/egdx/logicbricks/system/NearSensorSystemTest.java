package com.ilargia.games.egdx.logicbricks.system;


import com.ilargia.games.egdx.logicbricks.component.sensor.Link;
import com.ilargia.games.egdx.logicbricks.component.sensor.NearSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.egdx.logicbricks.system.sensor.NearSensorSystem;
import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.entitas.index.EntityIndex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NearSensorSystemTest {

    Entitas entitas;
    private EntitasCollections collections;
    private NearSensorSystem nearSensorSystem;
    private SensorEntity sensorEntity;
    private SensorEntity sensorEntity2;
    private GameEntity boss;
    private GameEntity groundEntity;
    private GameEntity playerEntity;
    private SensorEntity sensorEntity3;
    private SensorEntity sensorEntity4;


    public NearSensorSystemTest() {
        collections = new EntitasCollections(new CollectionsFactories() {});
        entitas = new Entitas();
        this.nearSensorSystem = new NearSensorSystem(entitas);
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
                .addNearSensor("Boss", 1, 1)
                .addLink(playerEntity.getCreationIndex());

        sensorEntity2 = entitas.sensor.createEntity()
                .addNearSensor("Ground", 1, 1)
                .addLink(playerEntity.getCreationIndex());

        sensorEntity3 = entitas.sensor.createEntity()
                .addCollisionSensor("Ground")
                .addLink(boss.getCreationIndex());

        sensorEntity4 = entitas.sensor.createEntity()
                .addCollisionSensor("Player")
                .addLink(boss.getCreationIndex());

    }


    @Test
    public void nearSensorTest() {

        nearSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(),"NearSensor", true);
        nearSensorSystem.execute( 0.5F);
        Link link = sensorEntity.getLink();
        NearSensor nearSensor = sensorEntity.getNearSensor();

        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        assertEquals(2, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals( 1, nearSensor.distanceContactList.size());
        assertTrue( nearSensor.distanceContactList.contains(boss.getCreationIndex()));

        nearSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        assertEquals(2, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals( 1, nearSensor.distanceContactList.size());
        assertTrue( nearSensor.distanceContactList.contains(boss.getCreationIndex()));


        nearSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "NearSensor",false);
        nearSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);


        assertEquals(2, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals( 0, nearSensor.distanceContactList.size());

        nearSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        nearSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), groundEntity.getCreationIndex(), "NearSensor",true);
        nearSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);
        assertEquals(2, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals(0, nearSensor.distanceContactList.size());

        nearSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);


    }

    @Test
    public void resetNearSensorTest() {

        nearSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(),"NearSensor", true);
        nearSensorSystem.execute( 0.5F);
        Link link = sensorEntity.getLink();
        NearSensor nearSensor = sensorEntity.getNearSensor();

        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        assertEquals( 1, nearSensor.distanceContactList.size());
        assertEquals( 0, nearSensor.resetDistanceContactList.size());
        assertTrue( nearSensor.distanceContactList.contains(boss.getCreationIndex()));

        nearSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "ResetNearSensor",true);
        nearSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        assertEquals( 1, nearSensor.distanceContactList.size());
        assertEquals( 1, nearSensor.resetDistanceContactList.size());
        assertTrue( nearSensor.distanceContactList.contains(boss.getCreationIndex()));


        nearSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "NearSensor",false);
        nearSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        assertEquals( 0, nearSensor.distanceContactList.size());
        assertEquals( 1, nearSensor.resetDistanceContactList.size());

        nearSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        nearSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "ResetNearSensor",false);
        nearSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        assertEquals(2, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals( 0, nearSensor.distanceContactList.size());
        assertEquals( 0, nearSensor.resetDistanceContactList.size());

        nearSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);
        assertEquals(2, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals( 0, nearSensor.distanceContactList.size());
        assertEquals( 0, nearSensor.resetDistanceContactList.size());


    }

}
