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

public class Collector<TEntity extends IEntity> implements ICollector{

    private Set<TEntity> _collectedEntities; //ObjectOpenHashSet
    private IGroup<TEntity>[] _groups;
    private GroupEvent[] _groupEvents;
    GroupChanged<TEntity> _addEntityCache;
    String _toStringCache;
    StringBuilder _toStringBuilder;


    public Collector(IGroup<TEntity> group, GroupEvent eventType) {
        this(new IGroup[]{group}, new GroupEvent[]{eventType});

    }

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

    @Override
    public Set collectedEntities() {
        return _collectedEntities;
    }

    @Override
    public int getCount() {
        return _collectedEntities.size();
    }

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

    @Override
    public void deactivate() {
        for (int i = 0; i < _groups.length; i++) {
            Group group = (Group) _groups[i];
            group.OnEntityAdded.remove(_addEntityCache);
            group.OnEntityRemoved.remove(_addEntityCache);

        }
        clearCollectedEntities();
    }

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
