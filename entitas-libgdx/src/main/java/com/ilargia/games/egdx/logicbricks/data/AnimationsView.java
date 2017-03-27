package com.ilargia.games.egdx.logicbricks.data;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Map;

public class AnimationsView extends TextureView {
    public Map<String, Animation<TextureRegion>> animationStates;
    public Animation<TextureRegion> currentAnimation;
    public float time;

    public AnimationsView(String reference, Bounds bounds, Map.Entry<String, Animation>... animations) {
        super(reference, bounds);
        for (Map.Entry<String, Animation> animation : animations) {
            animationStates.put(animation.getKey(), animation.getValue());
        }
        currentAnimation = animationStates.get(0);


    }
}
