package com.ilargia.games.entitas.system;


import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.entitas.index.EntityIndex;
import com.ilargia.games.logicbrick.component.sensor.Link;
import com.ilargia.games.logicbrick.data.Axis2D;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.index.SimpleGameIndex;
import com.ilargia.games.logicbrick.system.sensor.IndexingLinkSensorSystem;
import com.ilargia.games.logicbrick.system.sensor.RadarSensorSystem;
import com.ilargia.games.logicbrick.system.sensor.RaySensorSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RaySensorSystemTest {
    Entitas entitas;
    private EntitasCollections collections;
    private RaySensorSystem radarSensorSystem;
    private IndexingLinkSensorSystem linkSensorSystem;
    private SensorEntity sensorEntity;
    private SensorEntity sensorEntity2;
    private GameEntity boss;
    private GameEntity groundEntity;
    private GameEntity playerEntity;
    private SensorEntity sensorEntity3;
    private SensorEntity sensorEntity4;

    public RaySensorSystemTest() {
        collections = new EntitasCollections(new CollectionsFactories() {});
        entitas = new Entitas();
        this.radarSensorSystem = new RaySensorSystem(entitas);
        this.linkSensorSystem = new IndexingLinkSensorSystem(entitas);
        SimpleGameIndex.createGameEntityIndices(entitas.game);
        linkSensorSystem.activate();

        boss = entitas.game.createEntity()
                .addIdentity("Enemy","Boss");

        groundEntity = entitas.game.createEntity()
                .addIdentity("Ground","Ground");

        playerEntity = entitas.game.createEntity()
                .addIdentity("Player","Player1");

        sensorEntity = entitas.sensor.createEntity()
                .addRaySensor("Boss", Axis2D.Xnegative, 1,1)
                .addLink(playerEntity.getCreationIndex());

        sensorEntity2 = entitas.sensor.createEntity()
                .addRaySensor("Ground", Axis2D.Xnegative, 1,1)
                .addLink(playerEntity.getCreationIndex());

        sensorEntity4 = entitas.sensor.createEntity()
                .addRaySensor("Ground", Axis2D.Xnegative, 1,1)
                .addLink(boss.getCreationIndex());

        linkSensorSystem.execute(1);



    }


    @Test
    public void queryTrue() {
        EntityIndex<SensorEntity, Integer> eIndex = (EntityIndex<SensorEntity, Integer>)entitas.sensor.getEntityIndex("Sensors");
        EntityIndex<GameEntity, Integer> gameIndex = (EntityIndex<GameEntity, Integer>) entitas.game.getEntityIndex("GameEntities");
        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
        radarSensorSystem.execute( 0.5F);
        Link link = sensorEntity.getLink();

        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        assertEquals(2, eIndex.getEntities(playerEntity.getCreationIndex()).size());
        assertEquals( 1, gameIndex.getEntities(sensorEntity.getCreationIndex()).size());

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
        linkSensorSystem.execute(1);


        assertEquals(3, eIndex.getEntities(playerEntity.getCreationIndex()).size());
        assertEquals(0, gameIndex.getEntities(sensorEntity.getCreationIndex()).size());

        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        radarSensorSystem.processSensorCollision(playerEntity.getCreationIndex(), groundEntity.getCreationIndex(), "RadarSensor",  true);
        radarSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);
        assertEquals(3, eIndex.getEntities(playerEntity.getCreationIndex()).size());
        assertEquals(0, gameIndex.getEntities(sensorEntity.getCreationIndex()).size());

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
