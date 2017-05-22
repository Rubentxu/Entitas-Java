package ilargia.egdx.logicbricks.system.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ilargia.egdx.logicbricks.component.game.Animations;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.game.GameMatcher;
import ilargia.egdx.logicbricks.component.game.TextureView;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.entitas.api.system.IExecuteSystem;
import ilargia.entitas.group.Group;
import ilargia.entitas.matcher.Matcher;


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
