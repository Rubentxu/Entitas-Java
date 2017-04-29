package com.ilargia.games.egdx.logicbricks.component.scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.egdx.logicbricks.data.Bounds;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Scene"})
public class Background implements IComponent {
    public TextureRegion image ;

    public Background(TextureRegion image) {
        this.image = image;

    }

}