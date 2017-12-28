package ilargia.entitas.systems;

import ilargia.entitas.Entity;
import ilargia.entitas.api.IContexts;
import ilargia.entitas.api.system.IReactiveSystem;
import ilargia.entitas.collector.ICollector;
import ilargia.entitas.factories.EntitasCollections;

import java.util.List;

/**
 * A ReactiveSystem calls Execute(entities) if there were changes based on the specified Collector and will only pass in changed entities.
 * A common use-case is to react to changes, e.g. a change of the position of an entity to update the gameObject.transform.position
 * of the related gameObject.
 * @param <TEntity>
 * @param <TContexts>
 */
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

    /**
     *  Specify the collector that will trigger the ReactiveSystem.
     * @param context
     * @return
     */
    protected abstract ICollector[] getTrigger(TContexts context);

    /**
     * This will exclude all entities which don't pass the filter.
     * @param entity
     * @return
     */
    protected abstract boolean filter(TEntity entity);

    protected abstract void execute(List<TEntity> entities);

    /**
     * Activates the ReactiveSystem and starts observing changes based on the specified Collector.
     * ReactiveSystem are activated by default.
     */
    public void activate() {
        for (ICollector collector : _collectors) {
            collector.activate();
        }
    }

    /**
     * Deactivates the ReactiveSystem.
     * No changes will be tracked while deactivated.
     * This will also clear the ReactiveSystem.
     * ReactiveSystem are activated by default.
     */
    public void deactivate() {
        for (ICollector collector : _collectors) {
            collector.deactivate();
        }
    }

    /**
     * Clears all accumulated changes.
     */
    public void clear() {
        for (ICollector collector : _collectors) {
            collector.clearCollectedEntities();
        }
    }

    /**
     * Will call Execute(entities) with changed entities
     * if there are any. Otherwise it will not call Execute(entities).
     * @param deltatime
     */
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