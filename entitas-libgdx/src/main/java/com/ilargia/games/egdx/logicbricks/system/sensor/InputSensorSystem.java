package com.ilargia.games.egdx.logicbricks.system.sensor;

import com.badlogic.gdx.InputAdapter;
import com.ilargia.games.egdx.logicbricks.component.sensor.Frequency;
import com.ilargia.games.egdx.logicbricks.component.sensor.Link;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.entitas.api.system.IExecuteSystem;

public abstract class InputSensorSystem extends InputAdapter implements IExecuteSystem{

    public void process(SensorEntity sensor, float deltaTime) {
        Link link = sensor.getLink();
        boolean lastPulse = link.pulse;
        link.pulse = query(sensor, deltaTime);

        if (sensor.hasFrequency()) {
            Frequency frequency = sensor.getFrequency();
            if ((frequency.time += deltaTime) >= frequency.tick) {
                if (sensor.hasMode()) {
                    link.isOpen = link.isChanged || (sensor.getMode().type == link.pulse);
                } else {
                    link.isOpen = link.isChanged;
                }
                frequency.time = 0;
            } else {
                link.isOpen = false;

            }
        } else {
            if (sensor.hasMode()) {
                link.isOpen = (lastPulse != link.pulse) || (sensor.getMode().type == link.pulse);
            } else {
                link.isOpen = lastPulse != link.pulse;
            }
        }
        link.isChanged = lastPulse != link.pulse;


    }


    protected abstract boolean query(SensorEntity sensor, float deltaTime);
}

