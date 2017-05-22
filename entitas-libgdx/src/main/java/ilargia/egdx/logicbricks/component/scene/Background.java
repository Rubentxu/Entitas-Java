package ilargia.egdx.logicbricks.component.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Scene"})
public class Background implements IComponent {
    public TextureRegion image ;

    public Background(TextureRegion image) {
        this.image = image;

    }

}