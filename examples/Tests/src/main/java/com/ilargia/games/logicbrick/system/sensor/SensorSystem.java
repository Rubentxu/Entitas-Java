package com.ilargia.games.logicbrick.system.sensor;

import com.ilargia.games.logicbrick.component.sensor.Frequency;
import com.ilargia.games.logicbrick.component.sensor.Signal;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;

public abstract class SensorSystem {

    public void process(SensorEntity sensor, float deltaTime) {
        Signal signal = sensor.getSignal();
        boolean lastPulse = signal.pulse;
        signal.pulse = query(sensor, deltaTime);

        if (sensor.hasFrequency()) {
            Frequency frequency = sensor.getFrequency();
            if ((frequency.time += deltaTime) >= frequency.tick) {
                if (sensor.hasMode()) {
                    signal.isOpen = signal.isChanged || (sensor.getMode().type == signal.pulse);
                } else {
                    signal.isOpen = signal.isChanged;
                }
                frequency.time = 0;
            } else {
                signal.isOpen = false;

            }
        } else {
            if (sensor.hasMode()) {
                signal.isOpen = (lastPulse != signal.pulse) || (sensor.getMode().type == signal.pulse);
            } else {
                signal.isOpen = lastPulse != signal.pulse;
            }
        }
        signal.isChanged = lastPulse != signal.pulse;


    }


    protected abstract boolean query(SensorEntity sensor, float deltaTime);
}

