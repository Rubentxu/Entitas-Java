package com.ilargia.games.entitas.system;


import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.logicbrick.component.sensor.Link;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.index.GameIndex;
import com.ilargia.games.logicbrick.system.sensor.CollisionSensorSystem;
import com.ilargia.games.logicbrick.system.sensor.IndexingLinkSensorSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NearSensorSystemTest {
    private EntitasCollections collections;
    private CollisionSensorSystem collisionSensorSystem;
    private IndexingLinkSensorSystem linkSensorSystem;
    private SensorEntity sensorEntity;
    private GameEntity boss;
    private GameEntity groundEntity;
    private GameEntity playerEntity;


    @Before
    public void setUp() {
        collections = new EntitasCollections(new CollectionsFactories() {});
        Entitas entitas = new Entitas();
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

        GameIndex.createSensorEntityIndices(entitas.game);
        linkSensorSystem.execute(1);


    }

    @Test
    public void queryTrue() {
        collisionSensorSystem.processCollision(playerEntity, boss, true);
        collisionSensorSystem.execute( 0.5F);
        Link link = sensorEntity.getLink();

        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

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
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity, groundEntity, true);
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
