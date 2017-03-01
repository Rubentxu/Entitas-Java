package com.ilargia.games.core.component.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

import java.util.Map;

@Component(pools = {"Game"})
public class Animations implements IComponent {
    public Map<String, Animation<TextureRegion>> animationStates;
    public Animation<TextureRegion> currentAnimation;
    public float time;


}
