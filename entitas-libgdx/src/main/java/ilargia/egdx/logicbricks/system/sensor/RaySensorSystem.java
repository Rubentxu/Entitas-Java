package ilargia.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import ilargia.egdx.logicbricks.component.sensor.Link;
import ilargia.egdx.logicbricks.component.sensor.RaySensor;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorContext;
import ilargia.egdx.logicbricks.gen.sensor.SensorEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorMatcher;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.group.Group;
import ilargia.entitas.matcher.Matcher;

public class RaySensorSystem extends SensorSystem {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;
    private final World physics;

    public RaySensorSystem(Entitas entitas, World physisc) {
        this.sensorContex = entitas.sensor;
        this.physics = physisc;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.RaySensor(), SensorMatcher.Link()));

    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        RaySensor sensor = sensorEntity.getRaySensor();
        sensor.rayContactList.clear();
        sensor.collisionSignal = false;
        Link link = sensorEntity.getLink();

        float angle = sensor.axis2D.ordinal() * 90.0f;

        GameEntity originEntity =  Indexed.getInteractiveEntity(link.ownerEntity);

        Vector2 point1 = originEntity.getRigidBody().body.getPosition();
        Vector2 point2 = point1.cpy().add(new Vector2((float) MathUtils.cosDeg(angle), MathUtils.sinDeg(angle)).scl(sensor.range));

        physics.rayCast((fixture, point, normal, fraction) -> reportRayFixture(sensor, fixture), point1, point2);
        return sensor.collisionSignal;

    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
        }
    }

    float reportRayFixture (RaySensor sensor, Fixture fixture){
        Integer indexEntity = (Integer) fixture.getBody().getUserData();
        sensor.collisionSignal = false;
        GameEntity entity = Indexed.getInteractiveEntity(indexEntity);

        if (sensor.targetTag != null && entity.getTags().values.contains(sensor.targetTag)) {
            sensor.rayContactList.add(indexEntity);
            sensor.collisionSignal = true;
        } else if (sensor.targetTag == null ) {
            sensor.rayContactList.add(indexEntity);
            sensor.collisionSignal = true;
        }

        if (sensor.xRayMode) return 1;
        else return 0;
    }

}

