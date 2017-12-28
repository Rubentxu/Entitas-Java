package ilargia.entitas.collector;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.api.IGroup;
import ilargia.entitas.api.events.GroupChanged;
import ilargia.entitas.group.GroupEvent;
import ilargia.entitas.exceptions.CollectorException;
import ilargia.entitas.factories.EntitasCollections;
import ilargia.entitas.group.Group;

import java.util.Arrays;
import java.util.Set;

/**
 * A Collector can observe one or more groups from the same context
 * and collects changed entities based on the specified groupEvent.
 *
 * @author Rubentxu
 * @param <TEntity>
 */
public class Collector<TEntity extends IEntity> implements ICollector {


    private Set<TEntity> _collectedEntities; //ObjectOpenHashSet
    private IGroup<TEntity>[] _groups;
    private GroupEvent[] _groupEvents;
    GroupChanged<TEntity> _addEntityCache;
    String _toStringCache;
    StringBuilder _toStringBuilder;

    /**
     * Creates a Collector and will collect changed entities based on the specified groupEvent.
     * @param group
     * @param eventType
     */
    public Collector(IGroup<TEntity> group, GroupEvent eventType) {
        this(new IGroup[]{group}, new GroupEvent[]{eventType});

    }

    /**
     * Creates a Collector and will collect changed entities based on the specified groupEvents.
     * @param groups
     * @param groupEvents
     */
    public Collector(IGroup<TEntity>[] groups, GroupEvent[] groupEvents) {
        _groups = groups;
        _collectedEntities = EntitasCollections.createSet(IEntity.class);
        _groupEvents = groupEvents;

        if (groups.length != groupEvents.length) {
            throw new CollectorException("Unbalanced count with groups (" + groups.length +
                    ") and group events (" + groupEvents.length + ").",
                    "Group and group event count must be equal."
            );
        }

        _addEntityCache = (IGroup<TEntity> group, TEntity entity, int index, IComponent component) -> {
            addEntity(group, entity, index, component);
        };
        activate();
    }

    /**
     * Returns all collected entities.
     * Call collector.clearCollectedEntities() once you processed all entities.
     *
     * @returns Set<TEntity>
     */
    @Override
    public Set collectedEntities() {
        return _collectedEntities;
    }

    /**
     * @return int  Returns the number of all collected entities.
     */
    @Override
    public int getCount() {
        return _collectedEntities.size();
    }

    /**
     * Activates the Collector and will start collecting changed entities. Collectors are activated by default.
     */
    @Override
    public void activate() {
        for (int i = 0; i < _groups.length; i++) {
            Group group = (Group) _groups[i];
            GroupEvent groupEvent = _groupEvents[i];
            switch (groupEvent) {
                case Added:
                    group.OnEntityAdded(_addEntityCache);
                    break;
                case Removed:
                    group.OnEntityRemoved(_addEntityCache);
                    break;
                case AddedOrRemoved:
                    group.OnEntityAdded(_addEntityCache);
                    group.OnEntityRemoved(_addEntityCache);
                    break;
            }

        }
    }

    /**
     * Deactivates the Collector.
     * This will also clear all collected entities.
     * Collectors are activated by default.
     */
    @Override
    public void deactivate() {
        for (int i = 0; i < _groups.length; i++) {
            Group group = (Group) _groups[i];
            group.OnEntityAdded.remove(_addEntityCache);
            group.OnEntityRemoved.remove(_addEntityCache);

        }
        clearCollectedEntities();
    }

    /**
     *  Clears all collected entities.
     */
    @Override
    public void clearCollectedEntities() {
        for (IEntity entity : _collectedEntities) {
            entity.release(this);
        }
        _collectedEntities.clear();

    }

    void addEntity(IGroup<TEntity> group, TEntity entity, int index, IComponent component) {
        boolean added = _collectedEntities.add(entity);
        if (added) {
            entity.retain(this);
        }
    }

    @Override
    public String toString() {
        return "Collector{" +
                "_collectedEntities=" + _collectedEntities +
                ", _groups=" + Arrays.toString(_groups) +
                ", _groupEvents=" + Arrays.toString(_groupEvents) +
                ", _addEntityCache=" + _addEntityCache +
                ", _toStringCache='" + _toStringCache + '\'' +
                ", _toStringBuilder=" + _toStringBuilder +
                '}';
    }
}
