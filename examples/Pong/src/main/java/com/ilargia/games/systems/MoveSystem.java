package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.components.Motion;
import com.ilargia.games.components.View;
import com.ilargia.games.core.CoreContext;
import com.ilargia.games.core.CoreEntity;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;


public class MoveSystem implements IExecuteSystem {
    private Group<CoreEntity> _group;

    public MoveSystem(CoreContext context) {
        _group = context.getGroup(Matcher.AllOf(CoreMatcher.View(), CoreMatcher.Motion()));
    }

    @Override
    public void execute(float deltatime) {
        for (CoreEntity e : _group.getEntities()) {
            Motion motion = e.getMotion();
            View view = e.getView();

            if (view.shape instanceof Rectangle) {
                Rectangle ret = (Rectangle) view.shape;
                ret.setPosition(ret.x + motion.velocity.x * Gdx.graphics.getDeltaTime(),
                        ret.y + motion.velocity.y * Gdx.graphics.getDeltaTime());
            } else {
                Circle circle = (Circle) view.shape;
                circle.setPosition(circle.x + motion.velocity.x * Gdx.graphics.getDeltaTime()
                        , circle.y + motion.velocity.y * Gdx.graphics.getDeltaTime());
            }
        }
    }

}
