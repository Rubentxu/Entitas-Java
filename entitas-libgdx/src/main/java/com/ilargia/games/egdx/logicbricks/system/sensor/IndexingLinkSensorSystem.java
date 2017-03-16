package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.GameIndex;
import com.ilargia.games.egdx.logicbricks.index.SensorIndex;

import java.util.List;

public class IndexingLinkSensorSystem extends ReactiveSystem<SensorEntity> {
    private final SensorContext sensorContext;

    public IndexingLinkSensorSystem(Entitas entitas) {
        super(entitas.sensor);
        this.sensorContext = entitas.sensor;
        SensorIndex.createSensorsIndices(sensorContext);
        GameIndex.createGameEntitiesIndices(entitas.game);
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(SensorMatcher.Link());
    }

    @Override
    protected boolean filter(SensorEntity entity) {
        return entity.hasLink();
    }

    @Override
    protected void execute(List<SensorEntity> entities) {
        for (SensorEntity e : entities) {
            SensorIndex.addSensor(sensorContext, e.getLink().targetEntity, e);
        }

    }


}
