package ilargia.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.graphics.Color;
import ilargia.egdx.logicbricks.component.game.TextureView;
import ilargia.egdx.logicbricks.data.Bounds;
import ilargia.egdx.logicbricks.data.interfaces.Actuator;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class TextureActuator implements IComponent {
    public Actuator actuator;

    public TextureActuator( Bounds bounds, int opacity, Boolean flipX, Boolean flipY, Color tint) {
        this.actuator = (indexOwner)-> {
            GameEntity owner = Indexed.getInteractiveEntity(indexOwner);
            TextureView view = owner.getTextureView();
            if (bounds != null) view.bounds = bounds;
            if (flipX != null) view.flipX = flipX;
            if (flipY != null) view.flipY = flipY;
            if (opacity != -1) view.opacity = opacity;
            if (tint != null) view.tint = tint;
        };
    }


}
