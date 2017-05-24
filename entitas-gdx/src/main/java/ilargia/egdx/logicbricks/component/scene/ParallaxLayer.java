package ilargia.egdx.logicbricks.component.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Scene"})
public class ParallaxLayer implements IComponent {
    public TextureRegion background ;
    public Vector2 parallaxRatio;
    public Vector2 startPosition;
    public Vector2 padding ;
    public int layer;

    public ParallaxLayer(TextureRegion background, Vector2 parallaxRatio, Vector2 startPosition, Vector2 padding) {
        this.background = background;
        this.parallaxRatio = parallaxRatio;
        this.startPosition = startPosition;
        this.padding = padding;
        this.layer = 0;
    }

}