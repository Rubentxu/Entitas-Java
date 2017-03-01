package com.indignado.games.states.game.system;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.indignado.games.states.game.component.game.Animations;
import com.indignado.games.states.game.component.game.TextureView;
import com.indignado.games.states.game.gen.Entitas;
import com.indignado.games.states.game.gen.game.GameEntity;
import com.indignado.games.states.game.gen.game.GameMatcher;


public class AnimationSystem implements IExecuteSystem {

    private final Group<GameEntity> _groupAnimation;

    public AnimationSystem(Entitas entitas) {
        _groupAnimation = entitas.game.getGroup(Matcher.AllOf(GameMatcher.Animations(), GameMatcher.TextureView()));
    }

    @Override
    public void execute(float deltaTime) {

        for (GameEntity e : _groupAnimation.getEntities()) {
            Animations animations = e.getAnimations();
            TextureView view = e.getTextureView();
            animations.time += deltaTime;

            Animation<TextureRegion> animation = animations.currentAnimation;
            if (animation != null) {
                view.texture = animation.getKeyFrame(animations.time);

            }
        }
    }
}
