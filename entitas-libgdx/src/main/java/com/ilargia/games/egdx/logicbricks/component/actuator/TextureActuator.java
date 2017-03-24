package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.graphics.Color;
import com.ilargia.games.egdx.logicbricks.component.game.TextureView;
import com.ilargia.games.egdx.logicbricks.data.interfaces.Actuator;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.egdx.logicbricks.data.Bounds;

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
