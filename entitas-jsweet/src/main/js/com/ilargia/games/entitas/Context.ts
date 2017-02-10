/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas {
    import ContextInfo = com.ilargia.games.entitas.api.ContextInfo;

    import FactoryEntity = com.ilargia.games.entitas.api.FactoryEntity;

    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import IContext = com.ilargia.games.entitas.api.IContext;

    import IEntity = com.ilargia.games.entitas.api.IEntity;

    import IEntityIndex = com.ilargia.games.entitas.api.IEntityIndex;

    import IGroup = com.ilargia.games.entitas.api.IGroup;

    import ContextEntityChanged = com.ilargia.games.entitas.api.events.ContextEntityChanged;

    import ContextGroupChanged = com.ilargia.games.entitas.api.events.ContextGroupChanged;

    import EntityComponentChanged = com.ilargia.games.entitas.api.events.EntityComponentChanged;

    import EntityComponentReplaced = com.ilargia.games.entitas.api.events.EntityComponentReplaced;

    import EntityReleased = com.ilargia.games.entitas.api.events.EntityReleased;

    import GroupChanged = com.ilargia.games.entitas.api.events.GroupChanged;

    import IMatcher = com.ilargia.games.entitas.api.matcher.IMatcher;

    import EntitasCache = com.ilargia.games.entitas.caching.EntitasCache;

    import Collector = com.ilargia.games.entitas.collector.Collector;

    import GroupEvent = com.ilargia.games.entitas.events.GroupEvent;

    import ContextDoesNotContainEntityException = com.ilargia.games.entitas.exceptions.ContextDoesNotContainEntityException;

    import ContextEntityIndexDoesAlreadyExistException = com.ilargia.games.entitas.exceptions.ContextEntityIndexDoesAlreadyExistException;

    import ContextEntityIndexDoesNotExistException = com.ilargia.games.entitas.exceptions.ContextEntityIndexDoesNotExistException;

    import ContextInfoException = com.ilargia.games.entitas.exceptions.ContextInfoException;

    import ContextStillHasRetainedEntitiesException = com.ilargia.games.entitas.exceptions.ContextStillHasRetainedEntitiesException;

    import EntityIsNotDestroyedException = com.ilargia.games.entitas.exceptions.EntityIsNotDestroyedException;

    import Collections = com.ilargia.games.entitas.factories.Collections;

    import Group = com.ilargia.games.entitas.group.Group;

    import Array = java.lang.reflect.Array;

    import Arrays = java.util.Arrays;

    import List = java.util.List;

    import Map = java.util.Map;

    import Set = java.util.Set;

    import Stack = java.util.Stack;

    export class Context<TEntity extends Entity> implements IContext<TEntity> {
        public _totalComponents : number;

        public entityType : any;

        public __OnEntityCreated : Set<ContextEntityChanged> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextEntityChanged");

        public __OnEntityWillBeDestroyed : Set<ContextEntityChanged> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextEntityChanged");

        public __OnEntityDestroyed : Set<ContextEntityChanged> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextEntityChanged");

        public __OnGroupCreated : Set<ContextGroupChanged> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextGroupChanged");

        public __OnGroupCleared : Set<ContextGroupChanged> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextGroupChanged");

        _groups : Map<IMatcher<any>, Group<TEntity>>;

        _groupsForIndex : List<Group<TEntity>>[];

        _cachedEntityChanged : EntityComponentChanged<TEntity>;

        private _creationIndex : number;

        private _entities : Set<TEntity>;

        private _reusableEntities : Stack<TEntity>;

        private _retainedEntities : Set<TEntity>;

        private _entitiesCache : TEntity[];

        private _entityIndices : Map<string, IEntityIndex>;

        private _factoryEntiy : FactoryEntity<TEntity>;

        private _contextInfo : ContextInfo;

        private _componentContexts : Stack<IComponent>[];

        public constructor(totalComponents : number, startCreationIndex : number, contexInfo : ContextInfo, factoryMethod : FactoryEntity<TEntity>) {
            this._totalComponents = 0;
            this._creationIndex = 0;
            this._totalComponents = totalComponents;
            this._creationIndex = startCreationIndex;
            this._factoryEntiy = factoryMethod;
            if(contexInfo != null) {
                this._contextInfo = contexInfo;
                if(contexInfo.componentNames.length !== totalComponents) {
                    throw new ContextInfoException(this, contexInfo);
                }
            } else {
                let componentNames : string[] = new Array(totalComponents);
                let prefix : string = "Index ";
                for(let i : number = 0; i < componentNames.length; i++) {
                    componentNames[i] = prefix + i;
                }
                this._contextInfo = new ContextInfo("Unnamed SplashPool", componentNames, null);
            }
            this._groupsForIndex = new Array(this._totalComponents);
            this._componentContexts = new Array(totalComponents);
            this._entityIndices = Collections.createMap(String, "com.ilargia.games.entitas.api.IEntityIndex");
            this._reusableEntities = <any>(new Stack<any>());
            this._retainedEntities = Collections.createSet<any>(Entity);
            this._entities = Collections.createSet<any>(Entity);
            this._groups = Collections.createMap("com.ilargia.games.entitas.api.matcher.IMatcher", Group);
            this._cachedEntityChanged = (e, idx, c) => {
                this.updateGroupsComponentAddedOrRemoved(e, idx, c, this._groupsForIndex);
            };
            this.entityType = <any>(<any>this._factoryEntiy.create(this._totalComponents, this._componentContexts, this._contextInfo).constructor);
        }

        public createEntity() : TEntity {
            let ent : TEntity;
            if(this._reusableEntities.size() > 0) {
                ent = this._reusableEntities.pop();
                ent.reactivate(this._creationIndex++);
            } else {
                ent = this._factoryEntiy.create(this._totalComponents, this._componentContexts, this._contextInfo);
                ent.initialize(this._creationIndex++, this._totalComponents, this._componentContexts, this._contextInfo);
            }
            this._entities.add(<TEntity>ent);
            ent.retain(this);
            this._entitiesCache = null;
            let entity : Entity = <Entity>ent;
            entity.OnComponentAdded(this._cachedEntityChanged);
            entity.OnComponentRemoved(this._cachedEntityChanged);
            entity.OnComponentReplaced((e, idx, pc, nc) => {
                this.updateGroupsComponentReplaced(e, idx, pc, nc, this._groupsForIndex);
            });
            entity.OnEntityReleased((e) => {
                this.onEntityReleased(e, this._retainedEntities, this._reusableEntities);
            });
            this.notifyEntityCreated(ent);
            return ent;
        }

        public destroyEntity(entity? : any) : any {
            if(((entity != null) || entity === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    if(!this._entities.remove(entity)) {
                        throw new ContextDoesNotContainEntityException("\'" + this + "\' cannot destroy " + entity + "!", "Did you call pool.DestroyEntity() on a wrong pool?");
                    }
                    this._entitiesCache = null;
                    this.notifyEntityWillBeDestroyed(entity);
                    entity.destroy();
                    this.notifyEntityDestroyed(entity);
                    if(entity.retainCount() === 1) {
                        this._reusableEntities.push(entity);
                        entity.release(this);
                        entity.removeAllOnEntityReleasedHandlers();
                    } else {
                        this._retainedEntities.add(entity);
                        entity.release(this);
                    }
                })();
            } else throw new Error('invalid overload');
        }

        public destroyAllEntities() {
            {
                let array123 = this.getEntities();
                for(let index122=0; index122 < array123.length; index122++) {
                    let entity = array123[index122];
                    {
                        this.destroyEntity(entity);
                    }
                }
            }
            this._entities.clear();
            if(this._retainedEntities.size() !== 0) {
                throw new ContextStillHasRetainedEntitiesException(this);
            }
        }

        public hasEntity(entity? : any) : any {
            if(((entity != null) || entity === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    return this._entities.contains(entity);
                })();
            } else throw new Error('invalid overload');
        }

        public getEntities$() : TEntity[] {
            if(this._entitiesCache == null) {
                this._entitiesCache = <TEntity[]>Array.newInstance(this.entityType, this._entities.size());
                this._entities.toArray<any>(this._entitiesCache);
            }
            return this._entitiesCache;
        }

        public getTotalComponents() : number {
            return this._totalComponents;
        }

        public getGroup(matcher : IMatcher<any>) : Group<TEntity> {
            let group : Group<TEntity> = null;
            if(!(this._groups.containsKey(matcher)?(group = this._groups.get(matcher)) == null:false)) {
                group = <any>(new Group(matcher, this.entityType));
                {
                    let array125 = this.getEntities();
                    for(let index124=0; index124 < array125.length; index124++) {
                        let entity = array125[index124];
                        {
                            group.handleEntitySilently(entity);
                        }
                    }
                }
                this._groups.put(matcher, group);
                {
                    let array127 = matcher.getIndices();
                    for(let index126=0; index126 < array127.length; index126++) {
                        let index = array127[index126];
                        {
                            if(this._groupsForIndex[index] == null) {
                                this._groupsForIndex[index] = Collections.createList(Group);
                            }
                            this._groupsForIndex[index].add(group);
                        }
                    }
                }
                this.notifyGroupCreated(group);
            }
            return group;
        }

        public clearGroups() {
            for(let index128=this._groups.values().iterator();index128.hasNext();) {
                let group = index128.next();
                {
                    group.removeAllEventHandlers();
                    {
                        let array130 = group.getEntities();
                        for(let index129=0; index129 < array130.length; index129++) {
                            let entity = array130[index129];
                            {
                                entity.release(group);
                            }
                        }
                    }
                    this.notifyGroupCleared(group);
                }
            }
            this._groups.clear();
            for(let i : number = 0; i < this._groupsForIndex.length; i++) {
                this._groupsForIndex[i] = null;
            }
        }

        public addEntityIndex(name : string, entityIndex : IEntityIndex) {
            if(this._entityIndices.containsKey(name)) {
                throw new ContextEntityIndexDoesAlreadyExistException(this, name);
            }
            this._entityIndices.put(name, entityIndex);
        }

        public getEntityIndex(name : string) : IEntityIndex {
            let entityIndex : IEntityIndex;
            if(!this._entityIndices.containsKey(name)) {
                throw new ContextEntityIndexDoesNotExistException(this, name);
            } else {
                entityIndex = this._entityIndices.get(name);
            }
            return entityIndex;
        }

        public deactivateAndRemoveEntityIndices() {
            for(let index131=this._entityIndices.values().iterator();index131.hasNext();) {
                let entityIndex = index131.next();
                {
                    entityIndex.deactivate();
                }
            }
            this._entityIndices.clear();
        }

        public resetCreationIndex() {
            this._creationIndex = 0;
        }

        public clearComponentPool(index : number) {
            let componentPool : Stack<IComponent> = this._componentContexts[index];
            if(componentPool != null) {
                componentPool.clear();
            }
        }

        public clearComponentPools() {
            for(let i : number = 0; i < this._componentContexts.length; i++) {
                this.clearComponentPool(i);
            }
        }

        public reset() {
            this.clearGroups();
            this.destroyAllEntities();
            this.resetCreationIndex();
            this.clearEventsListener();
        }

        public updateGroupsComponentAddedOrRemoved(entity : TEntity, index : number, component : IComponent, groupsForIndex : List<Group<TEntity>>[]) {
            let groups : List<Group<TEntity>> = groupsForIndex[index];
            if(groups != null) {
                let events : List<Set<GroupChanged<any>>> = EntitasCache.getGroupChangedList();
                for(let i : number = 0; i < groups.size(); i++) {
                    events.add(groups.get(i).handleEntity(entity));
                }
                for(let i : number = 0; i < events.size(); i++) {
                    let groupChangedEvent : Set<GroupChanged<any>> = events.get(i);
                    if(groupChangedEvent != null) {
                        for(let index132=groupChangedEvent.iterator();index132.hasNext();) {
                            let listener = index132.next();
                            {
                                listener(groups.get(i), entity, index, component);
                            }
                        }
                    }
                }
                EntitasCache.pushGroupChangedList(events);
            }
        }

        updateGroupsComponentReplaced(entity : TEntity, index : number, previousComponent : IComponent, newComponent : IComponent, groupsForIndex : List<Group<TEntity>>[]) {
            let groups : List<Group<TEntity>> = groupsForIndex[index];
            if(groups != null) {
                for(let index133=groups.iterator();index133.hasNext();) {
                    let g = index133.next();
                    {
                        g.updateEntity(entity, index, previousComponent, newComponent);
                    }
                }
            }
        }

        onEntityReleased(entity : TEntity, retainedEntities : Set<TEntity>, reusableEntities : Stack<TEntity>) {
            if(entity.isEnabled()) {
                throw new EntityIsNotDestroyedException("Cannot release entity.");
            }
            entity.removeAllOnEntityReleasedHandlers();
            retainedEntities.remove(entity);
            reusableEntities.push(entity);
        }

        public getComponentPools() : Stack<IComponent>[] {
            return this._componentContexts;
        }

        public getContextInfo() : ContextInfo {
            return this._contextInfo;
        }

        public getCount() : number {
            return this._entities.size();
        }

        public getReusableEntitiesCount() : number {
            return this._reusableEntities.size();
        }

        public getRetainedEntitiesCount() : number {
            return this._retainedEntities.size();
        }

        public getEntities(matcher? : any) : any {
            if(((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    return this.getGroup(matcher).getEntities();
                })();
            } else if(matcher === undefined) {
                return <any>this.getEntities$();
            } else throw new Error('invalid overload');
        }

        public createCollector$com_ilargia_games_entitas_api_matcher_IMatcher(matcher : IMatcher<any>) : Collector<any> {
            return <any>(new Collector(this.getGroup(matcher), GroupEvent.Added));
        }

        public createCollector(matcher? : any, groupEvent? : any) : any {
            if(((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null) && ((typeof groupEvent === 'number') || groupEvent === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    return <any>(new Collector(this.getGroup(matcher), groupEvent));
                })();
            } else if(((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null) && groupEvent === undefined) {
                return <any>this.createCollector$com_ilargia_games_entitas_api_matcher_IMatcher(matcher);
            } else throw new Error('invalid overload');
        }

        public createEntityCollector$com_ilargia_games_entitas_Context_A$com_ilargia_games_entitas_api_matcher_IMatcher(contexts : Context<any>[], matcher : IMatcher<any>) : Collector<any> {
            return this.createEntityCollector(contexts, matcher, GroupEvent.Added);
        }

        public createEntityCollector(contexts? : any, matcher? : any, eventType? : any) : any {
            if(((contexts != null && contexts instanceof Array) || contexts === null) && ((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null) && ((typeof eventType === 'number') || eventType === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    let groups : Group<any>[] = new Array(contexts.length);
                    let eventTypes : GroupEvent[] = new Array(contexts.length);
                    for(let i : number = 0; i < contexts.length; i++) {
                        groups[i] = contexts[i].getGroup(matcher);
                        eventTypes[i] = eventType;
                    }
                    return <any>(new Collector(groups, eventTypes));
                })();
            } else if(((contexts != null && contexts instanceof Array) || contexts === null) && ((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null) && eventType === undefined) {
                return <any>this.createEntityCollector$com_ilargia_games_entitas_Context_A$com_ilargia_games_entitas_api_matcher_IMatcher(contexts, matcher);
            } else throw new Error('invalid overload');
        }

        public clearEventsListener() {
            if(this.__OnEntityCreated != null) this.__OnEntityCreated.clear();
            if(this.__OnEntityWillBeDestroyed != null) this.__OnEntityWillBeDestroyed.clear();
            if(this.__OnEntityDestroyed != null) this.__OnEntityDestroyed.clear();
            if(this.__OnGroupCreated != null) this.__OnGroupCreated.clear();
            if(this.__OnGroupCleared != null) this.__OnGroupCleared.clear();
        }

        public OnEntityCreated(listener : ContextEntityChanged) {
            if(this.__OnEntityCreated != null) {
                this.__OnEntityCreated = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextEntityChanged");
            }
            this.__OnEntityCreated.add(listener);
        }

        public OnEntityWillBeDestroyed(listener : ContextEntityChanged) {
            if(this.__OnEntityWillBeDestroyed != null) {
                this.__OnEntityWillBeDestroyed = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextEntityChanged");
            }
            this.__OnEntityWillBeDestroyed.add(listener);
        }

        public OnEntityDestroyed(listener : ContextEntityChanged) {
            if(this.__OnEntityDestroyed != null) {
                this.__OnEntityDestroyed = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextEntityChanged");
            }
            this.__OnEntityDestroyed.add(listener);
        }

        public OnGroupCreated(listener : ContextGroupChanged) {
            if(this.__OnGroupCreated != null) {
                this.__OnGroupCreated = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextGroupChanged");
            }
            this.__OnGroupCreated.add(listener);
        }

        public OnGroupCleared(listener : ContextGroupChanged) {
            if(this.__OnGroupCleared != null) {
                this.__OnGroupCleared = Collections.createSet<any>("com.ilargia.games.entitas.api.events.ContextGroupChanged");
            }
            this.__OnGroupCleared.add(listener);
        }

        public notifyEntityCreated(entity : IEntity) {
            if(this.__OnEntityCreated != null) {
                for(let index134=this.__OnEntityCreated.iterator();index134.hasNext();) {
                    let listener = index134.next();
                    {
                        listener(this, entity);
                    }
                }
            }
        }

        public notifyEntityWillBeDestroyed(entity : IEntity) {
            if(this.__OnEntityWillBeDestroyed != null) {
                for(let index135=this.__OnEntityWillBeDestroyed.iterator();index135.hasNext();) {
                    let listener = index135.next();
                    {
                        listener(this, entity);
                    }
                }
            }
        }

        public notifyEntityDestroyed(entity : IEntity) {
            if(this.__OnEntityDestroyed != null) {
                for(let index136=this.__OnEntityDestroyed.iterator();index136.hasNext();) {
                    let listener = index136.next();
                    {
                        listener(this, entity);
                    }
                }
            }
        }

        public notifyGroupCreated(group : IGroup<any>) {
            if(this.__OnGroupCreated != null) {
                for(let index137=this.__OnGroupCreated.iterator();index137.hasNext();) {
                    let listener = index137.next();
                    {
                        listener(this, group);
                    }
                }
            }
        }

        public notifyGroupCleared(group : IGroup<any>) {
            if(this.__OnGroupCleared != null) {
                for(let index138=this.__OnGroupCleared.iterator();index138.hasNext();) {
                    let listener = index138.next();
                    {
                        listener(this, group);
                    }
                }
            }
        }

        public equals(o : any) : boolean {
            if(this === o) return true;
            if(o == null || (<any>this.constructor) !== (<any>o.constructor)) return false;
            let context : Context<any> = <Context<any>>o;
            if(this._totalComponents !== context._totalComponents) return false;
            if(this.entityType != null?!this.entityType.equals(context.entityType):context.entityType != null) return false;
            return this._contextInfo != null?this._contextInfo.equals(context._contextInfo):context._contextInfo == null;
        }

        public hashCode() : number {
            let result : number = 0;
            result = 31 * result + this._totalComponents;
            result = 31 * result + (this.entityType != null?this.entityType.hashCode():0);
            result = 31 * result + (this._contextInfo != null?this._contextInfo.hashCode():0);
            return result;
        }

        public toString() : string {
            return "Context{_totalComponents=" + this._totalComponents + ", entityType=" + this.entityType + ", _groups=" + this._groups + ", _groupsForIndex=" + Arrays.toString(this._groupsForIndex) + ", _creationIndex=" + this._creationIndex + ", _entities=" + this._entities + ", _reusableEntities=" + this._reusableEntities + ", _retainedEntities=" + this._retainedEntities + ", _entitiesCache=" + Arrays.toString(this._entitiesCache) + ", _entityIndices=" + this._entityIndices + ", _factoryEntiy=" + this._factoryEntiy + ", _contextInfo=" + this._contextInfo + ", _componentContexts=" + Arrays.toString(this._componentContexts) + ", _cachedEntityChanged=" + this._cachedEntityChanged + '}';
        }
    }
    Context["__class"] = "com.ilargia.games.entitas.Context";
    Context["__interfaces"] = ["com.ilargia.games.entitas.api.IContext"];


}

