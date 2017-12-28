package ilargia.entitas;

import ilargia.entitas.api.entitas.EntityBaseFactory;
import ilargia.entitas.api.entitas.IAERC;
import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.api.entitas.IEntityIndex;
import ilargia.entitas.api.matcher.IMatcher;
import ilargia.entitas.caching.EntitasCache;
import ilargia.entitas.caching.ObjectPool;
import ilargia.entitas.collector.Collector;
import ilargia.entitas.group.GroupEvent;
import ilargia.entitas.factories.EntitasCollections;
import ilargia.entitas.group.Group;
import ilargia.entitas.collector.TriggerOnEvent;
import ilargia.entitas.api.ContextInfo;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.api.IContext;
import ilargia.entitas.api.IGroup;
import ilargia.entitas.api.events.*;
import ilargia.entitas.exceptions.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

/**
 * A context manages the lifecycle of entities and groups.
 * You can create and destroy entities and get groups of entities.
 * The prefered way to create a context is to use the generated methods
 * from the code generator, e.g. GameContext context = new GameContext();
 * @param <TEntity>
 */
public class Context<TEntity extends Entity> implements IContext<TEntity> {


    private final ObjectPool _groupChangedListPool;
    public int _totalComponents;
    public Class<TEntity> entityType;
    // Eventos
    /// Occurs when an entity gets created.
    public Set<ContextEntityChanged> OnEntityCreated = EntitasCollections.createSet(ContextEntityChanged.class);
    /// Occurs when an entity will be destroyed.
    public Set<ContextEntityChanged> OnEntityWillBeDestroyed = EntitasCollections.createSet(ContextEntityChanged.class);
    /// Occurs when an entity got destroyed.
    public Set<ContextEntityChanged> OnEntityDestroyed = EntitasCollections.createSet(ContextEntityChanged.class);
    /// Occurs when a group gets created for the first time.
    public Set<ContextGroupChanged> OnGroupCreated = EntitasCollections.createSet(ContextGroupChanged.class);

    protected Map<IMatcher, Group<TEntity>> _groups; //Object2ObjectArrayMap
    protected List<Group<TEntity>>[] _groupsForIndex; // ObjectArrayList
    protected EntityComponentChanged<TEntity> _cachedEntityChanged;
    protected EntityComponentReplaced<TEntity> _cachedComponentReplaced;
    protected EntityEvent<TEntity> _cachedEntityReleased;
    protected EntityEvent<TEntity> _cachedDestroyEntity;
    private int _creationIndex;
    private Set<TEntity> _entities; //ObjectOpenHashSet
    private Stack<TEntity> _reusableEntities;
    private Set<TEntity> _retainedEntities; //ObjectOpenHashSet
    private TEntity[] _entitiesCache;
    private Map<String, IEntityIndex> _entityIndices; // Map
    private EntityBaseFactory<TEntity> _factoryEntiy;
    private ContextInfo _contextInfo;
    private Stack<IComponent>[] _componentPools;
    private Function<TEntity, IAERC> _aercFactory;

    /**
     * The prefered way to create a context is to use the generated methods
     * from the code generator, e.g. GameContext context = new GameContext();
     * @param totalComponents
     * @param startCreationIndex
     * @param contexInfo
     * @param factoryMethod
     * @param aercFactory
     */
    public Context(int totalComponents, int startCreationIndex, ContextInfo contexInfo,
                   EntityBaseFactory<TEntity> factoryMethod, Function<TEntity, IAERC> aercFactory) {
        _totalComponents = totalComponents;
        _creationIndex = startCreationIndex;
        _factoryEntiy = factoryMethod;

        if (contexInfo != null) {
            _contextInfo = contexInfo;

            if (contexInfo.componentNames.length != totalComponents) {
                throw new ContextInfoException(this, contexInfo);
            }
        } else {
            _contextInfo = createDefaultContextInfo();
        }

        _aercFactory = aercFactory == null? (entity)-> new SafeAERC(entity) : aercFactory;

        _groupsForIndex = new List[_totalComponents];
        _componentPools = new Stack[totalComponents];
        _entityIndices = EntitasCollections.createMap(String.class, IEntityIndex.class);

        _reusableEntities = new Stack<>();
        _retainedEntities = (Set<TEntity>) EntitasCollections.<Entity>createSet(Entity.class);
        _entities = (Set<TEntity>) EntitasCollections.<Entity>createSet(Entity.class);
        _groups = EntitasCollections.createMap(IMatcher.class, Group.class);
        _groupChangedListPool = new ObjectPool<>(() -> {
            return EntitasCollections.createList(Integer.class);
        }, list -> list.clear());

        _cachedEntityChanged = (TEntity e, int idx, IComponent c) -> {
            updateGroupsComponentAddedOrRemoved(e, idx, c, _groupsForIndex);
        };

        _cachedComponentReplaced = (final TEntity e, final int idx, final IComponent pComponent, final IComponent nComponent) -> {
            updateGroupsComponentReplaced(e, idx, pComponent, nComponent, _groupsForIndex);
        };

        _cachedEntityReleased = (final TEntity e) -> {
            onEntityReleased(e);
        };

        _cachedDestroyEntity = (final TEntity e) -> {
            onDestroyEntity(e);
        };
        entityType = (Class<TEntity>) _factoryEntiy.create().getClass();

    }

    ContextInfo createDefaultContextInfo() {
        String[] componentNames = new String[_totalComponents];
        String prefix = "Index ";
        for (int i = 0; i < componentNames.length; i++) {
            componentNames[i] = prefix + i;
        }

        return new ContextInfo("Unnamed Context", componentNames, null);
    }


    /**
     * Creates a new entity or gets a reusable entity from the internal ObjectPool for entities.
     * @return Entity
     */
    @Override
    public TEntity createEntity() {
        TEntity ent;
        if (_reusableEntities.size() > 0) {
            ent = _reusableEntities.pop();
            ent.reactivate(_creationIndex++);
        } else {
            ent = _factoryEntiy.create();
            ent.initialize(_creationIndex++, _totalComponents, _componentPools, _contextInfo, _aercFactory.apply(ent));
        }

        _entities.add((TEntity) ent);
        ent.retain(this);
        _entitiesCache = null;
        Entity entity = (Entity) ent;
        entity.OnComponentAdded(_cachedEntityChanged);
        entity.OnComponentRemoved(_cachedEntityChanged);
        entity.OnComponentReplaced(_cachedComponentReplaced);
        entity.OnEntityReleased(_cachedEntityReleased);
        entity.OnDestroyEntity(_cachedDestroyEntity);

        notifyEntityCreated(ent);

        return ent;

    }


    /**
     *  Destroys all entities in the context.
     * Throws an exception if there are still retained entities.
     */
    @Override
    public void destroyAllEntities() {
        for (TEntity entity : getEntities()) {
            entity.destroy();
        }
        _entities.clear();

        if (_retainedEntities.size() != 0) {
            throw new ContextStillHasRetainedEntitiesException(this);
        }

    }

    /**
     *  Determines whether the context has the specified entity.
     * @param entity
     * @return boolean
     */
    @Override
    public boolean hasEntity(TEntity entity) {
        return _entities.contains(entity);
    }

    /**
     *
     * @return  Returns all entities which are currently in the context.
     */
    @Override
    public TEntity[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = (TEntity[]) Array.newInstance(entityType, _entities.size());
            _entities.toArray(_entitiesCache);
        }
        return _entitiesCache;

    }

    /**
     * The total amount of components an entity can possibly have.
     * This value is generated by the code generator,
     * e.g ComponentLookup.TotalComponents.
     * @return int
     */
    @Override
    public int getTotalComponents() {
        return _totalComponents;
    }

    /**
     * Returns a group for the specified matcher.
     * Calling context.GetGroup(matcher) with the same matcher will always
     * @param matcher
     * @return Group return the same instance of the group.
     */
    @Override
    public Group<TEntity> getGroup(IMatcher matcher) {
        Group<TEntity> group = _groups.get(matcher);
        if (group == null) {
            group = new Group(matcher, entityType);
            for (TEntity entity : getEntities()) {
                group.handleEntitySilently(entity);
            }
            _groups.put(matcher, group);

            for (int index : matcher.getIndices()) {
                if (_groupsForIndex[index] == null) {
                    _groupsForIndex[index] = EntitasCollections.<Group<TEntity>>createList(Group.class);
                }
                _groupsForIndex[index].add(group);
            }
            notifyGroupCreated(group);
            return group;
        }
        return group;

    }

    /**
     * Adds the IEntityIndex for the specified name.
     * There can only be one IEntityIndex per name.
     * @param entityIndex
     */
    @Override
    public void addEntityIndex(IEntityIndex entityIndex) {
        if (_entityIndices.containsKey(entityIndex.getName())) {
            throw new ContextEntityIndexDoesAlreadyExistException(this, entityIndex.getName());
        }
        _entityIndices.put(entityIndex.getName(), entityIndex);

    }

    /**
     * @param name
     * @return IEntityIndex Gets the IEntityIndex for the specified name.
     */
    @Override
    public IEntityIndex getEntityIndex(String name) {
        if (!_entityIndices.containsKey(name)) {
            throw new ContextEntityIndexDoesNotExistException(this, name);
        }
        return _entityIndices.get(name);

    }

    /**
     *  Resets the creationIndex back to 0.
     */
    @Override
    public void resetCreationIndex() {
        _creationIndex = 0;
    }


    /**
     * Clears the componentPool at the specified index.
     * @param index
     */
    @Override
    public void clearComponentPool(int index) {
        Stack<IComponent> componentPool = _componentPools[index];
        if (componentPool != null) {
            componentPool.clear();
        }
    }

    /**
     * Clears all componentPools.
     */
    @Override
    public void clearComponentPools() {
        for (int i = 0; i < _componentPools.length; i++) {
            clearComponentPool(i);
        }
    }

    /**
     * Resets the context (destroys all entities and resets creationIndex back to 0).
     */
    @Override
    public void reset() {
        destroyAllEntities();
        resetCreationIndex();
        clearEventsListener();

    }

    public void updateGroupsComponentAddedOrRemoved(TEntity entity, int index, IComponent component, List<Group<TEntity>>[] groupsForIndex) {
        List<Group<TEntity>> groups = groupsForIndex[index];
        if (groups != null) {
            List<Set<GroupChanged>> events = (List<Set<GroupChanged>>) _groupChangedListPool.get();

            for (int i = 0; i < groups.size(); i++) {
                events.add(groups.get(i).handleEntity(entity));
            }

            for (int i = 0; i < events.size(); i++) {
                Set<GroupChanged> groupChangedEvent = events.get(i);
                if (groupChangedEvent != null) {
                    for (GroupChanged listener : groupChangedEvent) {
                        listener.changed(groups.get(i), entity, index, component);
                    }

                }
            }
            _groupChangedListPool.push(events);
        }

    }

    protected void updateGroupsComponentReplaced(TEntity entity, int index, IComponent previousComponent,
                                                 IComponent newComponent, List<Group<TEntity>>[] groupsForIndex) {
        List<Group<TEntity>> groups = groupsForIndex[index];
        if (groups != null) {
            for (Group g : groups) {
                g.updateEntity(entity, index, previousComponent, newComponent);
            }
        }

    }

    protected void onEntityReleased(TEntity entity) {
        if (entity.isEnabled()) {
            throw new EntityIsNotDestroyedException("Cannot release " + entity);
        }
        entity.removeAllOnEntityReleasedHandlers();
        _retainedEntities.remove(entity);
        _reusableEntities.push(entity);
    }

    void onDestroyEntity(TEntity entity) {
        if (!_entities.remove(entity)) {
            throw new ContextDoesNotContainEntityException("'" + this + "' cannot destroy " + entity + "!",
                    "Did you call context.DestroyEntity() on a wrong context?");
        }
        _entitiesCache = null;
        notifyEntityWillBeDestroyed(entity);

        entity.internalDestroy();

        notifyEntityDestroyed(entity);

        if (entity.retainCount() == 1) {
            entity.OnEntityReleased(_cachedEntityReleased);
            _reusableEntities.push(entity);
            entity.release(this);
            entity.removeAllOnEntityReleasedHandlers();

        } else {
            _retainedEntities.add(entity);
            entity.release(this);
        }
    }

    /**
     * Returns all componentPools. componentPools is used to reuse removed components.
     * Removed components will be pushed to the componentPool.
     * Use entity.CreateComponent(index, type) to get a new or reusable component from the componentPool.
     * @return Stack<IComponent>[]
     */
    @Override
    public Stack<IComponent>[] getComponentPools() {
        return _componentPools;
    }

    /**
     * The contextInfo contains information about the context.
     * It's used to provide better error messages.
     * @return
     */
    @Override
    public ContextInfo getContextInfo() {
        return _contextInfo;
    }

    /**
     * @return int  Returns the number of entities in the context.
     */
    @Override
    public int getCount() {
        return _entities.size();
    }

    /**
     * Returns the number of entities in the internal ObjectPool for entities which can be reused.
     * @return int
     */
    @Override
    public int getReusableEntitiesCount() {
        return _reusableEntities.size();
    }

    /**
     * @return int Returns the number of entities that are currently retained by other objects (e.g. Group, Collector, ReactiveSystem).
     */
    @Override
    public int getRetainedEntitiesCount() {
        return _retainedEntities.size();
    }

    public IEntity[] getEntities(IMatcher matcher) {
        return getGroup(matcher).getEntities();

    }


    @Override
    public Collector createCollector(IMatcher matcher) {
        return new Collector(getGroup(matcher), GroupEvent.Added);
    }

    @Override
    public Collector createCollector(IMatcher matcher, GroupEvent groupEvent) {
        return new Collector(getGroup(matcher), groupEvent);
    }

    public Collector<TEntity> createCollector(Context context, TriggerOnEvent<TEntity>[] triggers) {
        Group[] groups = new Group[triggers.length];
        GroupEvent[] groupEvents = new GroupEvent[triggers.length];

        for (int i = 0; i < triggers.length; i++) {
            groups[i] = context.getGroup(triggers[i].matcher);
            groupEvents[i] = triggers[i].groupEvent;
        }
        return new Collector(groups, groupEvents);
    }

    public void clearEventsListener() {
        if (OnEntityCreated != null) OnEntityCreated.clear();
        if (OnEntityWillBeDestroyed != null) OnEntityWillBeDestroyed.clear();
        if (OnEntityDestroyed != null) OnEntityDestroyed.clear();
        if (OnGroupCreated != null) OnGroupCreated.clear();

    }

    public void OnEntityCreated(ContextEntityChanged listener) {
        if (OnEntityCreated != null) {
            OnEntityCreated = EntitasCollections.createSet(ContextEntityChanged.class);
        }
        OnEntityCreated.add(listener);
    }

    public void OnEntityWillBeDestroyed(ContextEntityChanged listener) {
        if (OnEntityWillBeDestroyed != null) {
            OnEntityWillBeDestroyed = EntitasCollections.createSet(ContextEntityChanged.class);
        }
        OnEntityWillBeDestroyed.add(listener);
    }

    public void OnEntityDestroyed(ContextEntityChanged listener) {
        if (OnEntityDestroyed != null) {
            OnEntityDestroyed = EntitasCollections.createSet(ContextEntityChanged.class);
        }
        OnEntityDestroyed.add(listener);
    }

    public void OnGroupCreated(ContextGroupChanged listener) {
        if (OnGroupCreated != null) {
            OnGroupCreated = EntitasCollections.createSet(ContextGroupChanged.class);
        }
        OnGroupCreated.add(listener);
    }


    public void notifyEntityCreated(IEntity entity) {
        if (OnEntityCreated != null) {
            for (ContextEntityChanged listener : OnEntityCreated) {
                listener.changed(this, entity);
            }
        }
    }


    public void notifyEntityWillBeDestroyed(IEntity entity) {
        if (OnEntityWillBeDestroyed != null) {
            for (ContextEntityChanged listener : OnEntityWillBeDestroyed) {
                listener.changed(this, entity);
            }
        }
    }


    public void notifyEntityDestroyed(IEntity entity) {
        if (OnEntityDestroyed != null) {
            for (ContextEntityChanged listener : OnEntityDestroyed) {
                listener.changed(this, entity);
            }
        }
    }


    public void notifyGroupCreated(IGroup group) {
        if (OnGroupCreated != null) {
            for (ContextGroupChanged listener : OnGroupCreated) {
                listener.changed(this, group);
            }
        }
    }

    @Override
    public String toString() {
        return "Context{" +
                "_totalComponents=" + _totalComponents +
                ", entityType=" + entityType +
                ", _groups=" + _groups +
                ", _groupsForIndex=" + Arrays.toString(_groupsForIndex) +
                ", _creationIndex=" + _creationIndex +
                ", _entities=" + _entities +
                ", _reusableEntities=" + _reusableEntities +
                ", _retainedEntities=" + _retainedEntities +
                ", _entitiesCache=" + Arrays.toString(_entitiesCache) +
                ", _entityIndices=" + _entityIndices +
                ", _factoryEntiy=" + _factoryEntiy +
                ", _contextInfo=" + _contextInfo +
                ", _componentPools=" + Arrays.toString(_componentPools) +
                ", _cachedEntityChanged=" + _cachedEntityChanged +
                '}';
    }
}