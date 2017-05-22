package ilargia.egdx.logicbricks.system;


import com.ilargia.games.egdx.impl.managers.LogManagerGDX;
import com.ilargia.games.egdx.logicbricks.component.sensor.Link;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.egdx.logicbricks.system.sensor.CollisionSensorSystem;
import ilargia.entitas.factories.CollectionsFactories;
import ilargia.entitas.factories.EntitasCollections;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollisionSensorSystemTest {
    Entitas entitas;
    private EntitasCollections collections;
    private CollisionSensorSystem collisionSensorSystem;
    private SensorEntity sensorEntity;
    private SensorEntity sensorEntity2;
    private GameEntity boss;
    private GameEntity groundEntity;
    private GameEntity playerEntity;
    private SensorEntity sensorEntity3;
    private SensorEntity sensorEntity4;

    public CollisionSensorSystemTest() {
        collections = new EntitasCollections(new CollectionsFactories() {});
        entitas = new Entitas();
        this.collisionSensorSystem = new CollisionSensorSystem(entitas, null);
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

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), true);
        collisionSensorSystem.execute( 0.5F);
        Link link = sensorEntity.getLink();

        assertTrue(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);

        assertEquals(2, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals( 1, Indexed.getEntitiesInSensor(sensorEntity).size());

        collisionSensorSystem.execute( 0.5F);
        assertTrue(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), false);
        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertTrue(link.isOpen);
        assertTrue(link.isChanged);
        sensorEntity3 = entitas.sensor.createEntity()
                .addCollisionSensor("Ground")
                .addLink(playerEntity.getCreationIndex());

        assertEquals(3, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals(0, Indexed.getEntitiesInSensor(sensorEntity).size());

        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), groundEntity.getCreationIndex(), true);
        collisionSensorSystem.execute( 0.5F);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);
        assertEquals(3, Indexed.getSensorsEntities(playerEntity).size());
        assertEquals(0, Indexed.getEntitiesInSensor(sensorEntity).size());

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
        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), false);
        collisionSensorSystem.execute( 0.5f);
        Link link = sensorEntity.getLink();

        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.execute( 0.5f);
        assertFalse(link.pulse);
        assertFalse(link.isOpen);
        assertFalse(link.isChanged);

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), true);
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
        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), true);
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

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), false);
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
        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), true);
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

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), false);
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
        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), true);
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

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), false);
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
        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), false);
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

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), true);
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
        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), true);
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

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), false);
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
        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), true);
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

        collisionSensorSystem.processCollision(playerEntity.getCreationIndex(), boss.getCreationIndex(), false);
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
