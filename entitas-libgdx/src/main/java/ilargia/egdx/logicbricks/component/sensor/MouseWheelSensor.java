package ilargia.egdx.logicbricks.component.sensor;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class MouseWheelSensor implements IComponent {
    public int amountScroll = 0;
    public boolean scrollSignal = false;


}

