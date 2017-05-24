package ilargia.egdx.logicbricks.component.scene;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Scene"}, isSingleEntity = true)
public class Camera implements IComponent {
    public com.badlogic.gdx.graphics.Camera camera;

}