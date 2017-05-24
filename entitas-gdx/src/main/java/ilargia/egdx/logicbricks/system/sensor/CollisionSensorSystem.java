package ilargia.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.physics.box2d.Fixture;
import ilargia.egdx.api.managers.listener.Collision;
import ilargia.egdx.impl.EngineGDX;
import ilargia.egdx.impl.managers.PhysicsManagerGDX;
import ilargia.egdx.logicbricks.component.sensor.CollisionSensor;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorContext;
import ilargia.egdx.logicbricks.gen.sensor.SensorEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorMatcher;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.group.Group;
import ilargia.entitas.matcher.Matcher;

public class CollisionSensorSystem extends SensorSystem implements IInitializeSystem, Collision<Fixture> {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;
    private final EngineGDX engine;

    public CollisionSensorSystem(Entitas entitas, EngineGDX engine) {
        this.sensorContex = entitas.sensor;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.CollisionSensor(), SensorMatcher.Link()));
        this.engine = engine;
    }

    @Override
    public void initialize() {
        engine.getManager(PhysicsManagerGDX.class).addListener(this);
    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        return sensorEntity.getCollisionSensor().collisionSignal;

    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
        }
    }

    @Override
    public void processCollision(Fixture colliderA, Fixture colliderB, boolean collisionSignal) {
        Integer indexEntityA = (Integer) colliderA.getBody().getUserData();
        Integer indexEntityB = (Integer) colliderB.getBody().getUserData();
        if (indexEntityA != null && indexEntityB != null) {
            GameEntity entityA =  Indexed.getInteractiveEntity(indexEntityA);
            GameEntity entityB =  Indexed.getInteractiveEntity(indexEntityB);
            if (entityA != null && entityB != null) {
                for (SensorEntity entity : sensorGroup.getEntities()) {
                    CollisionSensor collision = entity.getCollisionSensor();
                    if (entityB.getTags().values.contains(collision.targetTag)) {
                        if (collisionSignal) {
                            Indexed.addEntityInSensor(entity, entityB);
                        } else {
                            Indexed.removeEntityInSensor(entity, entityB);
                        }
                        collision.collisionSignal = collisionSignal;
                    }
                }
            }
        }
    }
}

