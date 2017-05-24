package ilargia.egdx.logicbricks.component.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Scene"}, isSingleEntity = true)
public class GUI implements IComponent {
    public Stage stage;

}