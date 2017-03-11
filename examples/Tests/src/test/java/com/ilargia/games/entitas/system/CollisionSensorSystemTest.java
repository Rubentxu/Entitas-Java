package com.ilargia.games.entitas.system;


import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.entitas.index.EntityIndex;
import com.ilargia.games.logicbrick.component.sensor.Link;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.system.sensor.CollisionSensorSystem;
import com.ilargia.games.logicbrick.system.sensor.IndexingLinkSensorSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollisionSensorSystemTest {
    Entitas entitas;
    private EntitasCollections collections;
    private CollisionSensorSystem collisionSensorSystem;
    private IndexingLinkSensorSystem linkSensorSystem;
    private SensorEntity sensorEntity;
    private SensorEntity sensorEntity2;
    private GameEntity boss;
    private GameEntity groundEntity;
    private GameEntity playerEntity;
    private SensorEntity sensorEntity3;
    private SensorEntity sensorEntity4;

    @Before
    public void setUp() {
        collections = new EntitasCollections(new CollectionsFactories() {});
        entitas = new Entitas();
        this.collisionSensorSystem = new CollisionSensorSystem(entitas);
        this.linkSensorSystem = new IndexingLinkSensorSystem(entitas);
        linkSensorSystem.activate();

        boss = entitas.game.createEntity()
                .addIdentity("Enemy","Boss");

        groundEntity = entitas.game.createEntity()
                .addIdentity("Ground","Ground");

        playerEntity = entitas.game.createEntity()
                .addIdentity("Player","Player1");

        sensorEntity = entitas.sensor.createEntity()
                .addCollisionSensor("Boss")
                .addLink(playerEntity.getCreationIndex());

        sensorEntity2 = entitas.sensor.createEntity()
                .addCollisionSensor("Ground")
                .addLink(playerEntity.getCreationIndex());

        sensorEntity4 = entitas.sensor.createEntity()
                .addCollisionSensor("Ground")
                .addLink(boss.getCreationIndex());

        linkSensorSystem.execute(1);

    }

    @Test
    public void queryTrue() {
        EntityIndex<SensorEntity, Integer> eIndex = (EntityIndex<SensorEntity, Integer>)entitas.sensor.getEntityIndex("Sensors");
        EntityIndex<GameEntity, Integer> gameIndex = (EntityIndex<GameEntity, Integer>) entitas.game.getEntityIndex("GameEntities");
        collisionSensorSystem.processCollision(playerEntity, boss, true);
        collisionSensorSystem.execute( 0.5F);
        Link link = sensorEntity.getLink();


        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        assertEquals(2, eIndex.getEntities(playerEntity.getCreationIndex()).size());
        assertEquals( 1, gameIndex.getEntities(sensorEntity.getCreationIndex()).size());

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, boss, false);
        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);
        sensorEntity3 = entitas.sensor.createEntity()
                .addCollisionSensor("Ground")
                .addLink(playerEntity.getCreationIndex());
        linkSensorSystem.execute(1);


        assertEquals(3, eIndex.getEntities(playerEntity.getCreationIndex()).size());
        assertEquals(0, gameIndex.getEntities(sensorEntity.getCreationIndex()).size());

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, groundEntity, true);
        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);
        assertEquals(3, eIndex.getEntities(playerEntity.getCreationIndex()).size());
        assertEquals(0, gameIndex.getEntities(sensorEntity.getCreationIndex()).size());

        collisionSensorSystem.execute( 0.5F);
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
        collisionSensorSystem.processCollision(playerEntity, boss, false);
        collisionSensorSystem.execute( 0.5f);
        Link link = sensorEntity.getLink();

        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, boss, true);
        collisionSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void queryTrueAndModeTrue() {
        collisionSensorSystem.processCollision(playerEntity, boss, true);
        sensorEntity.addMode(true);
        Link link = sensorEntity.getLink();

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, boss, false);
        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
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
        collisionSensorSystem.processCollision(playerEntity, boss, true);
        sensorEntity.addMode(false);
        Link link = sensorEntity.getLink();

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, boss, false);
        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
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
        collisionSensorSystem.processCollision(playerEntity, boss, true);
        sensorEntity.addFrequency(1);
        Link link = sensorEntity.getLink();

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, boss, false);
        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void frecuencyAndQueryFalse() {
        collisionSensorSystem.processCollision(playerEntity, boss, false);
        sensorEntity.addFrequency(1);
        Link link = sensorEntity.getLink();

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, boss, true);
        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }


    @Test
    public void frecuencyAndQueryTrueAndModeTrue() {
        collisionSensorSystem.processCollision(playerEntity, boss, true);
        sensorEntity.addFrequency(1).addMode(true);
        Link link = sensorEntity.getLink();

        collisionSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, boss, false);
        collisionSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

    }

    @Test
    public void frecuencyAndQueryFalseAndModeFalse() {
        collisionSensorSystem.processCollision(playerEntity, boss, true);
        sensorEntity.addFrequency(1).addMode(false);
        Link link = sensorEntity.getLink();

        collisionSensorSystem.execute( 0.5f);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, boss, false);
        collisionSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertTrue(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertFalse(link.isChanged);

    }

}
