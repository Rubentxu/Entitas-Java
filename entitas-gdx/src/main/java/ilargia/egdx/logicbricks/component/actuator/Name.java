package ilargia.egdx.logicbricks.component.actuator;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class Name implements IComponent {
    public String nameReference;

    public Name(String nameReference) {
        this.nameReference = nameReference;

    }

}
