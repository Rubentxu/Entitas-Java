package com.ilargia.games.entitas;

import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.api.FactoryEntity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.index.IEntityIndex;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.api.events.EntityComponentReplaced;
import com.ilargia.games.entitas.api.events.EntityComponentChanged;
import com.ilargia.games.entitas.api.events.EntityReleased;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class BaseContext<E extends Entity, P extends BaseContext> {

    public int _totalComponents;
    public Class<E> entityType;
    protected Map<IMatcher, Group<E>> _groups; //Object2ObjectArrayMap
    protected List<Group<E>>[] _groupsForIndex; // ObjectArrayList
    private int _creationIndex;
    private Set<E> _entities; //ObjectOpenHashSet
    private Stack<E> _reusableEntities;
    private Set<E> _retainedEntities; //ObjectOpenHashSet
    private E[] _entitiesCache;
    private Map<String, IEntityIndex> _entityIndices; // Map
    private FactoryEntity<E> _factoryEntiy;
    private ContextInfo _contextInfo;
    private Stack<IComponent>[] _componentContexts;
    private EventBus<E> _eventBus;


    public BaseContext(int totalComponents, int startCreationIndex, ContextInfo metaData,
                       EventBus<E> eventBus, FactoryEntity<E> factoryMethod) {
        _totalComponents = totalComponents;
        _creationIndex = startCreationIndex;
        _factoryEntiy = factoryMethod;
        _eventBus = eventBus;

        if (metaData != null) {
            _contextInfo = metaData;

            if (metaData.componentNames.length != totalComponents) {
                throw new ContextInfoException(this, metaData);
            }
        } else {
            String[] componentNames = new String[totalComponents];
            String prefix = "Index ";
            for (int i = 0; i < componentNames.length; i++) {
                componentNames[i] = prefix + i;
            }
            _contextInfo = new ContextInfo(
                    "Unnamed SplashPool", componentNames, null
            );
        }

        _groupsForIndex = new List[_totalComponents];
        _componentContexts = new Stack[totalComponents];
        _entityIndices = Collections.createMap(String.class, IEntityIndex.class);

        _reusableEntities = new Stack<>();
        _retainedEntities = Collections.createSet(Entity.class);
        _entities = Collections.createSet(Entity.class);
        _groups = Collections.createMap(IMatcher.class, Group.class);

        EntityComponentChanged<E> _cachedEntityChanged = (E e, int idx, IComponent c) -> {
            updateGroupsComponentAddedOrRemoved(e, idx, c, _groupsForIndex);
        };
        _eventBus.OnComponentAdded.addListener(_cachedEntityChanged);
        _eventBus.OnComponentRemoved.addListener(_cachedEntityChanged);
        _eventBus.OnComponentReplaced.addListener((EntityComponentReplaced<E>) (E e, int idx, IComponent pc, IComponent nc) -> {
            updateGroupsComponentReplaced(e, idx, pc, nc, _groupsForIndex);
        });
        _eventBus.OnEntityReleased.addListener((EntityReleased<E>) (E e) -> {
            onEntityReleased(e, _retainedEntities, _reusableEntities);
        });

        entityType = (Class<E>) _factoryEntiy.create(_totalComponents, _componentContexts, _contextInfo).getClass();

    }

    public Collector createEntityCollector(BaseContext[] contexts, IMatcher matcher) {
        return createEntityCollector(contexts, matcher, GroupEvent.Added);
    }

    public Collector createEntityCollector(BaseContext[] contexts, IMatcher matcher, GroupEvent eventType) {
        Group[] groups = new Group[contexts.length];
        GroupEvent[] eventTypes = new GroupEvent[contexts.length];

        for (int i = 0; i < contexts.length; i++) {
            groups[i] = contexts[i].getGroup(matcher);
            eventTypes[i] = eventType;
        }

        return new Collector(groups, eventTypes, _eventBus);
    }

    public E createEntity() {
        E ent = _reusableEntities.size() > 0
                ? _reusableEntities.pop()
                : _factoryEntiy.create(_totalComponents, _componentContexts, _contextInfo);
        ent.setEnabled(true);
        ent.setCreationIndex(_creationIndex++);
        ent.retain(this);
        _entities.add(ent);
        _entitiesCache = null;
        _eventBus.notifyEntityCreated((P) this, ent);

        return ent;

    }

    public void destroyEntity(E entity) {
        if (!_entities.remove(entity)) {
            throw new ContextDoesNotContainEntityException("'" + this + "' cannot destroy " + entity + "!",
                    "Did you call pool.DestroyEntity() on a wrong pool?");
        }
        _entitiesCache = null;
        _eventBus.notifyEntityWillBeDestroyed((P) this, entity);

        entity.destroy();

        _eventBus.notifyEntityDestroyed((P) this, entity);

        if (entity.getRetainCount() == 1) {
            _reusableEntities.push(entity);
            entity.release(this);

        } else {
            _retainedEntities.add(entity);
            entity.release(this);
        }

    }

    public void destroyAllEntities() {
        for (E entity : getEntities()) {
            destroyEntity(entity);
        }
        _entities.clear();

        if (_retainedEntities.size() != 0) {
            throw new ContextStillHasRetainedEntitiesException(this);
        }

    }

    public boolean hasEntity(E entity) {
        return _entities.contains(entity);
    }

    public E[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = (E[]) new Entity[_entities.size()];
            _entities.toArray(_entitiesCache);
        }
        return _entitiesCache;

    }

    public Group<E> getGroup(IMatcher matcher) {
        Group<E> group = null;
        if (!(_groups.containsKey(matcher) ? (group = _groups.get(matcher)) == group : false)) {

            group = new Group(matcher, entityType,_eventBus);
            for (E entity : getEntities()) {
                group.handleEntitySilently(entity);
            }
            _groups.put(matcher, group);

            for (int index : matcher.getIndices()) {
                if (_groupsForIndex[index] == null) {
                    _groupsForIndex[index] = Collections.createList(Group.class);
                }
                _groupsForIndex[index].add(group);
            }
            _eventBus.notifyGroupCreated((P) this, group);

        }
        return group;

    }

    public void clearGroups() {
        for (Group<E> group : _groups.values()) {
            group.removeAllEventHandlers();
            for (Entity entity : group.getEntities()) {
                entity.release(group);
            }
            _eventBus.notifyGroupCleared((P) this, group);
        }
        _groups.clear();

        for (int i = 0; i < _groupsForIndex.length; i++) {
            _groupsForIndex[i] = null;
        }
    }

    public void addEntityIndex(String name, IEntityIndex entityIndex) {
        if (_entityIndices.containsKey(name)) {
            throw new ContextEntityIndexDoesAlreadyExistException(this, name);
        }
        _entityIndices.put(name, entityIndex);

    }

    public IEntityIndex getEntityIndex(String name) {
        IEntityIndex entityIndex;
        if (!_entityIndices.containsKey(name)) {
            throw new ContextEntityIndexDoesNotExistException(this, name);
        } else {
            entityIndex = _entityIndices.get(name);
        }
        return entityIndex;

    }

    public void deactivateAndRemoveEntityIndices() {
        for (IEntityIndex entityIndex : _entityIndices.values()) {
            entityIndex.deactivate();
        }
        _entityIndices.clear();
    }

    public void resetCreationIndex() {
        _creationIndex = 0;
    }

    public void clearComponentPool(int index) {
        Stack<IComponent> componentPool = _componentContexts[index];
        if (componentPool != null) {
            componentPool.clear();
        }
    }

    public void clearComponentPools() {
        for (int i = 0; i < _componentContexts.length; i++) {
            clearComponentPool(i);
        }
    }

    public void reset() {
        clearGroups();
        destroyAllEntities();
        resetCreationIndex();
        _eventBus.clearEventsPool();

    }

    public void updateGroupsComponentAddedOrRemoved(E entity, int index, IComponent component, List<Group<E>>[] groupsForIndex) {
        List<Group<E>> groups = groupsForIndex[index];
        if (groups != null) {
            for (int i = 0, groupsCount = groups.size(); i < groupsCount; i++) {
                groups.get(i).handleEntity(entity, index, component);
            }
        }

    }

    protected void updateGroupsComponentReplaced(E entity, int index, IComponent previousComponent,
                                                 IComponent newComponent, List<Group<E>>[] groupsForIndex) {
        List<Group<E>> groups = groupsForIndex[index];
        if (groups != null) {
            for (Group g : groups) {
                g.updateEntity(entity, index, previousComponent, newComponent);
            }
        }

    }

    protected void onEntityReleased(E entity, Set<E> retainedEntities, Stack<E> reusableEntities) {
        if (entity.isEnabled()) {
            throw new EntityIsNotDestroyedException("Cannot release entity.");
        }
        retainedEntities.remove(entity);
        reusableEntities.push(entity);
    }

    public Stack<IComponent>[] getComponentPools() {
        return _componentContexts;
    }

    public ContextInfo getMetaData() {
        return _contextInfo;
    }

    public int getCount() {
        return _entities.size();
    }

    public int getReusableEntitiesCount() {
        return _reusableEntities.size();
    }

    public int getRetainedEntitiesCount() {
        return _retainedEntities.size();
    }

    public Entity[] getEntities(IMatcher matcher) {
        return getGroup(matcher).getEntities();

    }


}