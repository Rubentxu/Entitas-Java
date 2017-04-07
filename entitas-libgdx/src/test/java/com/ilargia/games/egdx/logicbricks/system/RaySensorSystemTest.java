package com.ilargia.games.egdx.logicbricks.system;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.logicbricks.component.sensor.Link;
import com.ilargia.games.egdx.logicbricks.component.sensor.RaySensor;
import com.ilargia.games.egdx.logicbricks.data.Axis2D;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.egdx.logicbricks.system.sensor.RaySensorSystem;
import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.entitas.index.EntityIndex;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


public class RaySensorSystemTest {
    Entitas entitas;
    private EntitasCollections collections;
    private RaySensorSystem raySensorSystem;
    private SensorEntity sensorEntity;
    private SensorEntity sensorEntity2;
    private GameEntity boss;
    private GameEntity groundEntity;
    private GameEntity playerEntity;
    private SensorEntity sensorEntity3;
    private SensorEntity sensorEntity4;
    World world;


    public RaySensorSystemTest() {
        collections = new EntitasCollections(new CollectionsFactories() {});
        entitas = new Entitas();
        world = new World();
        Body body = Mockito.mock(Body.class);
        when(body.getPosition()).thenReturn(new Vector2());

        this.raySensorSystem = new RaySensorSystem(entitas, world);
        Indexed.initialize(entitas);

        boss = entitas.game.createEntity()
                .addTags("Enemy", "Boss")
                .addRigidBody(body)
                .setInteractive(true);

        groundEntity = entitas.game.createEntity()
                .addTags("Ground", "Ground")
                .addRigidBody(body)
                .setInteractive(true);

        playerEntity = entitas.game.createEntity()
                .addTags("Player", "Player1")
                .addRigidBody(body)
                .setInteractive(true);

        sensorEntity = entitas.sensor.createEntity()
                .addRaySensor("Boss", Axis2D.Xnegative, 1, false)
                .addLink(playerEntity.getCreationIndex(),"");

        sensorEntity2 = entitas.sensor.createEntity()
                .addRaySensor("Ground", Axis2D.Xnegative, 1, false)
                .addLink(playerEntity.getCreationIndex(),"");

        sensorEntity4 = entitas.sensor.createEntity()
                .addRaySensor("Ground", Axis2D.Xnegative, 1, false)
                .addLink(boss.getCreationIndex(),"");

    }


   // @Test
    public void queryTrue() {
        world.setIndexEntity(boss.getCreationIndex());
        raySensorSystem.execute(0.5F);
        Link link = sensorEntity.getLink();
        RaySensor sensor = sensorEntity.getRaySensor();

        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);
        assertEquals(1, sensor.rayContactList.size());

        raySensorSystem.execute(0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);
        assertEquals(1, sensor.rayContactList.size());

        world.setIndexEntity(groundEntity.getCreationIndex());
        raySensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);
        assertEquals(0, sensor.rayContactList.size());

        raySensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);
        assertEquals(0, sensor.rayContactList.size());

        world.setIndexEntity(groundEntity.getCreationIndex());
        raySensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);
        assertEquals(0, sensor.rayContactList.size());

        raySensorSystem.execute(0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);


    }
//
//    /* Pulses
//    * ....
//    * ------------
//    * */
//    @Test
//    public void queryFalse() {
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
//        raySensorSystem.execute(0.5f);
//        Link link = sensorEntity.getLink();
//
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5f);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
//        raySensorSystem.execute(0.5f);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5f);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//    }
//
//    @Test
//    public void queryTrueAndModeTrue() {
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
//        sensorEntity.addMode(true);
//        Link link = sensorEntity.getLink();
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertTrue(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//    }
//
//    /* Pulses
//    * ....
//    * ------------
//    * */
//    @Test
//    public void queryFalseAndModeFalse() {
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
//        sensorEntity.addMode(false);
//        Link link = sensorEntity.getLink();
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertTrue(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//    }
//
//    /* Pulses
//    * .:......
//    * ------------
//    * */
//    @Test
//    public void frecuencyAndQueryTrue() {
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
//        sensorEntity.addFrequency(1);
//        Link link = sensorEntity.getLink();
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5f);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5f);
//        assertFalse(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//    }
//
//    @Test
//    public void frecuencyAndQueryFalse() {
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
//        sensorEntity.addFrequency(1);
//        Link link = sensorEntity.getLink();
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5f);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5f);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//    }
//
//
//    @Test
//    public void frecuencyAndQueryTrueAndModeTrue() {
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
//        sensorEntity.addFrequency(1).addMode(true);
//        Link link = sensorEntity.getLink();
//
//        raySensorSystem.execute(0.5f);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
//        raySensorSystem.execute(0.5f);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//    }
//
//    @Test
//    public void frecuencyAndQueryFalseAndModeFalse() {
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", true);
//        sensorEntity.addFrequency(1).addMode(false);
//        Link link = sensorEntity.getLink();
//
//        raySensorSystem.execute(0.5f);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertTrue(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.processSensorCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), "RadarSensor", false);
//        raySensorSystem.execute(0.5f);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertTrue(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertFalse(link.isOpen);
//        assertFalse(link.isChanged);
//
//        raySensorSystem.execute(0.5F);
//        assertFalse(link.pulse);
//        assertTrue(link.isOpen);
//        assertFalse(link.isChanged);
//
//    }

}
