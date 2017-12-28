package ilargia.egdx.logicbricks.index;

import ilargia.egdx.logicbricks.component.actuator.Link;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.actuator.ActuatorEntity;
import ilargia.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import ilargia.egdx.logicbricks.gen.sensor.SensorMatcher;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.game.GameMatcher;
import ilargia.egdx.logicbricks.gen.sensor.SensorEntity;
import ilargia.entitas.index.EntityIndex;
import ilargia.entitas.index.PrimaryEntityIndex;
import ilargia.entitas.matcher.Matcher;

import java.util.Set;

public class Indexed {
    public static final String GameEntitiesInSensorIndex = "GameEntitiesInSensorIndex";
    public static final String SensorsEntityIndex = "SensorsEntityIndex";
    public static final String ActuatorsEntityIndex = "ActuatorsEntityIndex";
    public static final String InteractiveEntityIndex = "InteractiveEntityIndex";
    public static final String TagEntityIndex = "TagEntityIndex";

    private static Entitas _entitas;
    private static KeyIndex index;


    public static void initialize(Entitas entitas) {
        _entitas = entitas;
         index = new KeyIndex(-1,null);
        // GameEntity contains Sensors entities
        _entitas.sensor.addEntityIndex(new PrimaryEntityIndex<SensorEntity, KeyIndex>(Indexed.SensorsEntityIndex,
                ((e, c) -> {
                    if(c != null) {
                        ilargia.egdx.logicbricks.component.sensor.Link link= (ilargia.egdx.logicbricks.component.sensor.Link) c;
                        return new KeyIndex(link.ownerEntity,link.sensorReference);
                    }
                    return new KeyIndex(e.getLink().ownerEntity, e.getLink().sensorReference);
                }), _entitas.sensor.getGroup(SensorMatcher.Link())));
        // GameEntity contains Actuator entities
        _entitas.actuator.addEntityIndex(new PrimaryEntityIndex<ActuatorEntity, KeyIndex>(Indexed.ActuatorsEntityIndex,
                ((e, c) -> {
                    if(c != null) {
                        Link link= (Link) c;
                        return new KeyIndex(link.ownerEntity, link.actuatorReference);
                    }
                    return new KeyIndex(e.getLink().ownerEntity, e.getLink().actuatorReference);
                }), _entitas.actuator.getGroup(ActuatorMatcher.Link())));

        // Interactive GameEntity index
        _entitas.game.addEntityIndex(new PrimaryEntityIndex<GameEntity, Integer>(Indexed.InteractiveEntityIndex,
                ((e, c) -> e.getCreationIndex()), _entitas.game.getGroup(GameMatcher.Interactive())));

        // Tags GameEntity index
        _entitas.game.addEntityIndex(new EntityIndex<GameEntity, String>(Indexed.TagEntityIndex,
                _entitas.game.getGroup(Matcher.AllOf(GameMatcher.Tags(),GameMatcher.Interactive())),
                ((e, c) -> e.getTags().values.toArray(new String[0]))));

        // Sensors context GameEntities
        _entitas.game.addEntityIndex(new EntityIndex<GameEntity, Integer>(Indexed.GameEntitiesInSensorIndex,
                _entitas.game.getGroup(GameMatcher.Interactive()),
                ((e, c) -> new Integer[0])));




    }


    public static Set<GameEntity> getEntitiesInSensor(SensorEntity entity) {
        EntityIndex<GameEntity, Integer> eIndex = (EntityIndex<GameEntity, Integer>) _entitas.game.getEntityIndex(GameEntitiesInSensorIndex);
        return eIndex.getEntities(entity.getCreationIndex());
    }

    public static void addEntityInSensor(SensorEntity entity, GameEntity gameEntity) {
        EntityIndex<GameEntity, Integer> eIndex = (EntityIndex<GameEntity, Integer>) _entitas.game.getEntityIndex(GameEntitiesInSensorIndex);
        eIndex.addEntity(entity.getCreationIndex(), gameEntity);
    }

    public static void removeEntityInSensor(SensorEntity entity, GameEntity gameEntity) {
        EntityIndex<GameEntity, Integer> eIndex = (EntityIndex<GameEntity, Integer>) _entitas.game.getEntityIndex(GameEntitiesInSensorIndex);
        eIndex.removeEntity(entity.getCreationIndex(), gameEntity);
    }

    public static SensorEntity getSensorsEntity(GameEntity entity, String nameSensor) {
       return getSensorsEntity(entity.getCreationIndex(),nameSensor);
    }

    public static SensorEntity getSensorsEntity(int indexGameEntity, String nameSensor) {
        PrimaryEntityIndex<SensorEntity, KeyIndex> eIndex = (PrimaryEntityIndex<SensorEntity, KeyIndex>) _entitas.sensor.getEntityIndex(SensorsEntityIndex);
        return eIndex.getEntity(index.setIndex(indexGameEntity,nameSensor));
    }

    public static ActuatorEntity getActuatorEntity( GameEntity entity, String nameActuator) {
        return getActuatorEntity(entity.getCreationIndex(),nameActuator);
    }

    public static ActuatorEntity getActuatorEntity( int indexGameEntity, String nameActuator) {
        PrimaryEntityIndex<ActuatorEntity, KeyIndex> eIndex = (PrimaryEntityIndex<ActuatorEntity, KeyIndex>) _entitas.sensor.getEntityIndex(ActuatorsEntityIndex);
        return eIndex.getEntity(index.setIndex(indexGameEntity,nameActuator));
    }

    public static GameEntity getInteractiveEntity(Integer indexEntity) {
        PrimaryEntityIndex<GameEntity, Integer> eIndex = (PrimaryEntityIndex<GameEntity, Integer>) _entitas.game.getEntityIndex(InteractiveEntityIndex);
        return eIndex.getEntity(indexEntity);
    }

    public static Set<GameEntity> getTagEntities(String tag) {
        EntityIndex<GameEntity, String> eIndex = (EntityIndex<GameEntity, String>) _entitas.game.getEntityIndex(TagEntityIndex);
        return eIndex.getEntities(tag);
    }


}
