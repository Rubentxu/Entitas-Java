package com.ilargia.games.entitas.systems;

import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.system.IReactiveSystem;
import com.ilargia.games.entitas.factories.Collections;

import java.util.List;

public abstract class ReactiveSystem<TEntity extends IEntity> implements IReactiveSystem {

    protected Collector<TEntity> _collector;
    protected List<TEntity> _buffer;
    protected String _toStringCache;

    protected ReactiveSystem(IContext<TEntity> context) {
        _collector = getTrigger(context);
        _buffer = Collections.createList(Entity.class);
    }

    protected ReactiveSystem(Collector<TEntity> collector) {
        _collector = collector;
        _buffer = Collections.createList(Entity.class);
    }

    protected abstract Collector<TEntity> getTrigger(IContext<TEntity> context);

    protected abstract boolean filter(TEntity entity);

    protected abstract void execute(List<TEntity> entities);

    public void activate() {
        _collector.activate();
    }

    public void deactivate() {
        _collector.deactivate();
    }

    public void clear() {
        _collector.clearCollectedEntities();
    }

    public void execute(float deltatime) {
        if (_collector._collectedEntities.size() != 0) {
            for (TEntity e : _collector._collectedEntities) {
                if (filter(e)) {
                    e.retain(this);
                    _buffer.add(e);
                }
            }
            _collector.clearCollectedEntities();

            if (_buffer.size() != 0) {
                execute(_buffer);
                for (int i = 0; i < _buffer.size(); i++) {
                    _buffer.get(i).release(this);
                }
                _buffer.clear();
            }
        }
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            _toStringCache = "ReactiveSystem(" + getClass().getName() + ")";
        }

        return _toStringCache;
    }

}