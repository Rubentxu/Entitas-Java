package com.ilargia.games.logicbrick.system.sensor;

import com.ilargia.games.logicbrick.component.sensor.Frequency;
import com.ilargia.games.logicbrick.component.sensor.Link;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;

public abstract class SensorSystem {

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

