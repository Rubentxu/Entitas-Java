package ilargia.egdx.logicbricks.component.sensor;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Mode implements IComponent {
    public boolean type;

}
