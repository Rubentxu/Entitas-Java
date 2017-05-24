package ilargia.egdx.logicbricks.component.sensor;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class PointerOverSensor implements IComponent {
    public int pointer;
    public String targetTag;

    public PointerOverSensor(int pointer, String targetTag) {
        this.targetTag = targetTag;

    }

}

