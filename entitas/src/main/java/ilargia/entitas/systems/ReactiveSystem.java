package ilargia.entitas.systems;

import ilargia.entitas.Entity;
import ilargia.entitas.api.IContext;
import ilargia.entitas.api.system.IReactiveSystem;
import ilargia.entitas.collector.ICollector;
import ilargia.entitas.factories.EntitasCollections;

import java.util.List;

/**
 * A ReactiveSystem calls Execute(entities) if there were changes based on the specified Collector and will only pass in changed entities.
 * A common use-case is to react to changes, e.g. a change of the position of an entity to update the gameObject.transform.position
 * of the related gameObject.
 * @param <TEntity>
 */
public abstract class ReactiveSystem<TEntity extends Entity> implements IReactiveSystem {

    protected ICollector<TEntity> _collector;
    protected List<TEntity> _buffer;
    protected String _toStringCache;

    protected ReactiveSystem(IContext<TEntity> context) {
        _collector = getTrigger(context);
        _buffer = EntitasCollections.createList(Entity.class);
    }

    protected ReactiveSystem(ICollector<TEntity> collector) {
        _collector = collector;
        _buffer = EntitasCollections.createList(Entity.class);
    }

    /**
     * Specify the collector that will trigger the ReactiveSystem.
     * @param context
     * @return ICollector<TEntity>
     */
    protected abstract ICollector<TEntity> getTrigger(IContext<TEntity> context);

    /**
     * This will exclude all entities which don't pass the filter.
     * @param entity
     * @return boolean
     */
    protected abstract boolean filter(TEntity entity);

    protected abstract void execute(List<TEntity> entities);

    /**
     * Activates the ReactiveSystem and starts observing changes based on the specified Collector.
     * ReactiveSystem are activated by default.
     */
    public void activate() {
        _collector.activate();
    }

    /**
     * Deactivates the ReactiveSystem.
     * No changes will be tracked while deactivated.
     * This will also clear the ReactiveSystem.
     * ReactiveSystem are activated by default.
     */
    public void deactivate() {
        _collector.deactivate();
    }

    /**
     * Clears all accumulated changes.
     */
    public void clear() {
        _collector.clearCollectedEntities();
    }

    /**
     *  Will call execute(entities) with changed entities
     * if there are any. Otherwise it will not call Execute(entities).
     * @param deltatime
     */
    public void execute(float deltatime) {
        if (_collector.getCount() != 0) {
            for (TEntity e : _collector.collectedEntities()) {
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
        return "ReactiveSystem{" +
                "_collectors=" + _collector +
                ", _buffer=" + _buffer +
                ", _toStringCache='" + _toStringCache + '\'' +
                '}';
    }

}