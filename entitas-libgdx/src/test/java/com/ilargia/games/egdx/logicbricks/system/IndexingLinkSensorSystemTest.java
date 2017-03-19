package com.ilargia.games.egdx.logicbricks.system;


import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.system.sensor.IndexingSystem;
import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.entitas.index.EntityIndex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IndexingLinkSensorSystemTest {
    Entitas entitas;
    private EntitasCollections collections;
    private IndexingSystem linkSensorSystem;
    private SensorEntity sensorEntity;
    private SensorEntity sensorEntity2;
    private GameEntity boss;
    private GameEntity groundEntity;
    private GameEntity playerEntity;
    private SensorEntity sensorEntity3;
    private SensorEntity sensorEntity4;

    public IndexingLinkSensorSystemTest() {
        collections = new EntitasCollections(new CollectionsFactories() {});
        entitas = new Entitas();
        this.linkSensorSystem = new IndexingSystem(entitas);

        boss = entitas.game.createEntity()
                .addTags("Enemy","Boss");

        groundEntity = entitas.game.createEntity()
                .addTags("Ground","Ground");

        playerEntity = entitas.game.createEntity()
                .addTags("Player","Player1");

        sensorEntity = entitas.sensor.createEntity()
                .addCollisionSensor("Boss")
                .addLink(playerEntity.getCreationIndex());

        sensorEntity2 = entitas.sensor.createEntity()
                .addCollisionSensor("Ground")
                .addLink(playerEntity.getCreationIndex());

        sensorEntity4 = entitas.sensor.createEntity()
                .addCollisionSensor("Ground")
                .addLink(boss.getCreationIndex());

    }


    @Test
    public void queryTrue() {
        EntityIndex<SensorEntity, Integer> sensorIndex = (EntityIndex<SensorEntity, Integer>)entitas.sensor.getEntityIndex("Sensors");
        EntityIndex<GameEntity, Integer> gameIndex = (EntityIndex<GameEntity, Integer>) entitas.game.getEntityIndex("GameEntities");

        assertEquals(2, sensorIndex.getEntities(playerEntity.getCreationIndex()).size());
        assertEquals( 0, gameIndex.getEntities(sensorEntity.getCreationIndex()).size());

        sensorEntity3 = entitas.sensor.createEntity()
                .addCollisionSensor("Ground")
                .addLink(playerEntity.getCreationIndex());

        assertEquals(3, sensorIndex.getEntities(playerEntity.getCreationIndex()).size());
        assertEquals(0, gameIndex.getEntities(playerEntity.getCreationIndex()).size());

        assertEquals(3, sensorIndex.getEntities(playerEntity.getCreationIndex()).size());
        assertEquals(0, gameIndex.getEntities(sensorEntity.getCreationIndex()).size());


    }

}
