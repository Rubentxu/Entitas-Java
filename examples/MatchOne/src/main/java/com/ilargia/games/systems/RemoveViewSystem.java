package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.core.component.game.TextureView;
import com.ilargia.games.core.gen.game.GameContext;
import com.ilargia.games.core.gen.game.GameEntity;
import com.ilargia.games.core.gen.game.GameMatcher;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.group.GroupEvent;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;

public class RemoveViewSystem extends ReactiveSystem<GameEntity> implements ICleanupSystem {

    private final int BOX2D_VELOCITY_ITERATIONS = 6;
    private final int BOX2D_POSITION_ITERATIONS = 10;
    private World physics;

    public RemoveViewSystem(GameContext context, World world) {
        super(context);
        this.physics = world;

    }

    @Override
    public Collector<GameEntity> getTrigger(IContext<GameEntity> context) {
        return new Collector(new IGroup[]{
                context.getGroup(GameMatcher.Asset()),
                context.getGroup(GameMatcher.Destroy())
        },
                new GroupEvent[]{
                        GroupEvent.Removed,
                        GroupEvent.Added
                }
        );
    }

    @Override
    public boolean filter(GameEntity entity) {
        return entity.hasTextureView();
    }

    @Override
    protected void execute(List<GameEntity> entities) {
        for (GameEntity e : entities) {
            destroyView(e.getTextureView());
            e.removeTextureView();

        }
    }

    private void destroyView(TextureView textureView) {
        physics.destroyBody(textureView.body);
    }


    @Override
    public void cleanup() {
        physics.step(Gdx.graphics.getDeltaTime(), BOX2D_VELOCITY_ITERATIONS, BOX2D_POSITION_ITERATIONS);
    }


}
