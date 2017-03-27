package com.ilargia.games.egdx.logicbricks.system.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ilargia.games.egdx.logicbricks.data.AnimationsView;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.egdx.logicbricks.data.TextureView;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;


public class AnimationSystem implements IExecuteSystem {

    private final Group<GameEntity> _groupAnimation;

    public AnimationSystem(Entitas entitas) {
        _groupAnimation = entitas.game.getGroup(Matcher.AllOf(GameMatcher.Animations(), GameMatcher.TextureView()));
    }

    @Override
    public void execute(float deltaTime) {

        for (GameEntity e : _groupAnimation.getEntities()) {
            AnimationsView animations = e.getAnimations();
            TextureView view = e.getTextureView();
            animations.time += deltaTime;

            Animation<TextureRegion> animation = animations.currentAnimation;
            if (animation != null) {
                view.texture = animation.getKeyFrame(animations.time);

            }
        }
    }
}
