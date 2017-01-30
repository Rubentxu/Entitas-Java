package com.ilargia.games.systems;

import com.ilargia.games.components.TextureView;
import com.ilargia.games.core.Entitas;
import com.ilargia.games.core.GameEntity;
import com.ilargia.games.core.GameMatcher;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;

public class RemoveViewSystem extends ReactiveSystem<GameEntity> {

    public RemoveViewSystem(Entitas entitas) {
        super(entitas.game);
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

    }
}
