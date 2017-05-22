package ilargia.egdx.logicbricks.component.scene;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Scene"}, isSingleEntity = true)
public class Tiled implements IComponent {
    public String tileMapName;
    public float unitScale;

}