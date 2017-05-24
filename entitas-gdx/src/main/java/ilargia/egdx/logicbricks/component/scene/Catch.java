package ilargia.egdx.logicbricks.component.scene;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Scene"}, isSingleEntity = true)
public class Catch implements IComponent {
    public boolean catchBack = true;
    public boolean catchMenu = true;

}
