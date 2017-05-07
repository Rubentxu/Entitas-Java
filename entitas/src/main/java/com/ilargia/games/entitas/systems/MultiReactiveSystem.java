package com.ilargia.games.entitas.systems;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IContexts;
import com.ilargia.games.entitas.api.system.IReactiveSystem;
import com.ilargia.games.entitas.collector.ICollector;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.List;

public abstract class MultiReactiveSystem<TEntity extends Entity, TContexts extends IContexts> implements IReactiveSystem {

    protected ICollector<TEntity>[] _collectors;
    protected List<TEntity> _buffer;
    protected String _toStringCache;

    protected MultiReactiveSystem(TContexts context) {
        _collectors = getTrigger(context);
        _buffer = EntitasCollections.createList(Entity.class);
    }

    protected MultiReactiveSystem(ICollector[] collector) {
        _collectors = collector;
        _buffer = EntitasCollections.createList(Entity.class);
    }

    protected abstract ICollector[] getTrigger(TContexts context);

    protected abstract boolean filter(TEntity entity);

    protected abstract void execute(List<TEntity> entities);

    public void activate() {
        for (ICollector collector : _collectors) {
            collector.activate();
        }
    }

    public void deactivate() {
        for (ICollector collector : _collectors) {
            collector.deactivate();
        }
    }

    public void clear() {
        for (ICollector collector : _collectors) {
            collector.clearCollectedEntities();
        }
    }

    public void execute(float deltatime) {
        for (ICollector<TEntity> collector : _collectors) {
            if (collector.getCount() != 0) {
                for (TEntity e : collector.collectedEntities()) {
                    if (filter(e)) {
                        e.retain(this);
                        _buffer.add(e);
                    }
                }
                collector.clearCollectedEntities();
            }
        }

        if (_buffer.size() != 0) {
            execute(_buffer);
            for (int i = 0; i < _buffer.size(); i++) {
                _buffer.get(i).release(this);
            }
            _buffer.clear();
        }

    }

    @Override
    public String toString() {
        return "MultiReactiveSystem{" +
                "_collectors=" + _collectors +
                ", _buffer=" + _buffer +
                ", _toStringCache='" + _toStringCache + '\'' +
                '}';
    }

}