/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var ContextInfo = com.ilargia.games.entitas.api.ContextInfo;
                var EntitasCache = com.ilargia.games.entitas.caching.EntitasCache;
                var Collector = com.ilargia.games.entitas.collector.Collector;
                var GroupEvent = com.ilargia.games.entitas.events.GroupEvent;
                var ContextDoesNotContainEntityException = com.ilargia.games.entitas.exceptions.ContextDoesNotContainEntityException;
                var ContextEntityIndexDoesAlreadyExistException = com.ilargia.games.entitas.exceptions.ContextEntityIndexDoesAlreadyExistException;
                var ContextEntityIndexDoesNotExistException = com.ilargia.games.entitas.exceptions.ContextEntityIndexDoesNotExistException;
                var ContextInfoException = com.ilargia.games.entitas.exceptions.ContextInfoException;
                var ContextStillHasRetainedEntitiesException = com.ilargia.games.entitas.exceptions.ContextStillHasRetainedEntitiesException;
                var EntityIsNotDestroyedException = com.ilargia.games.entitas.exceptions.EntityIsNotDestroyedException;
                var Collections = com.ilargia.games.entitas.factories.Collections;
                var Group = com.ilargia.games.entitas.group.Group;
                var Array = java.lang.reflect.Array;
                var Arrays = java.util.Arrays;
                var Stack = java.util.Stack;
                var Context = (function () {
                    function Context(totalComponents, startCreationIndex, contexInfo, factoryMethod) {
                        var _this = this;
                        this.__OnEntityCreated = Collections.createSet("com.ilargia.games.entitas.api.events.ContextEntityChanged");
                        this.__OnEntityWillBeDestroyed = Collections.createSet("com.ilargia.games.entitas.api.events.ContextEntityChanged");
                        this.__OnEntityDestroyed = Collections.createSet("com.ilargia.games.entitas.api.events.ContextEntityChanged");
                        this.__OnGroupCreated = Collections.createSet("com.ilargia.games.entitas.api.events.ContextGroupChanged");
                        this.__OnGroupCleared = Collections.createSet("com.ilargia.games.entitas.api.events.ContextGroupChanged");
                        this._totalComponents = 0;
                        this._creationIndex = 0;
                        this._totalComponents = totalComponents;
                        this._creationIndex = startCreationIndex;
                        this._factoryEntiy = factoryMethod;
                        if (contexInfo != null) {
                            this._contextInfo = contexInfo;
                            if (contexInfo.componentNames.length !== totalComponents) {
                                throw new ContextInfoException(this, contexInfo);
                            }
                        }
                        else {
                            var componentNames = new Array(totalComponents);
                            var prefix = "Index ";
                            for (var i = 0; i < componentNames.length; i++) {
                                componentNames[i] = prefix + i;
                            }
                            this._contextInfo = new ContextInfo("Unnamed SplashPool", componentNames, null);
                        }
                        this._groupsForIndex = new Array(this._totalComponents);
                        this._componentContexts = new Array(totalComponents);
                        this._entityIndices = Collections.createMap(String, "com.ilargia.games.entitas.api.IEntityIndex");
                        this._reusableEntities = (new Stack());
                        this._retainedEntities = Collections.createSet(Entity);
                        this._entities = Collections.createSet(Entity);
                        this._groups = Collections.createMap("com.ilargia.games.entitas.api.matcher.IMatcher", Group);
                        this._cachedEntityChanged = function (e, idx, c) {
                            _this.updateGroupsComponentAddedOrRemoved(e, idx, c, _this._groupsForIndex);
                        };
                        this.entityType = this._factoryEntiy.create(this._totalComponents, this._componentContexts, this._contextInfo).constructor;
                    }
                    Context.prototype.createEntity = function () {
                        var _this = this;
                        var ent;
                        if (this._reusableEntities.size() > 0) {
                            ent = this._reusableEntities.pop();
                            ent.reactivate(this._creationIndex++);
                        }
                        else {
                            ent = this._factoryEntiy.create(this._totalComponents, this._componentContexts, this._contextInfo);
                            ent.initialize(this._creationIndex++, this._totalComponents, this._componentContexts, this._contextInfo);
                        }
                        this._entities.add(ent);
                        ent.retain(this);
                        this._entitiesCache = null;
                        var entity = ent;
                        entity.OnComponentAdded(this._cachedEntityChanged);
                        entity.OnComponentRemoved(this._cachedEntityChanged);
                        entity.OnComponentReplaced(function (e, idx, pc, nc) {
                            _this.updateGroupsComponentReplaced(e, idx, pc, nc, _this._groupsForIndex);
                        });
                        entity.OnEntityReleased(function (e) {
                            _this.onEntityReleased(e, _this._retainedEntities, _this._reusableEntities);
                        });
                        this.notifyEntityCreated(ent);
                        return ent;
                    };
                    Context.prototype.destroyEntity = function (entity) {
                        var _this = this;
                        if (((entity != null) || entity === null)) {
                            var __args = Array.prototype.slice.call(arguments);
                            return (function () {
                                if (!_this._entities.remove(entity)) {
                                    throw new ContextDoesNotContainEntityException("\'" + _this + "\' cannot destroy " + entity + "!", "Did you call pool.DestroyEntity() on a wrong pool?");
                                }
                                _this._entitiesCache = null;
                                _this.notifyEntityWillBeDestroyed(entity);
                                entity.destroy();
                                _this.notifyEntityDestroyed(entity);
                                if (entity.retainCount() === 1) {
                                    _this._reusableEntities.push(entity);
                                    entity.release(_this);
                                    entity.removeAllOnEntityReleasedHandlers();
                                }
                                else {
                                    _this._retainedEntities.add(entity);
                                    entity.release(_this);
                                }
                            })();
                        }
                        else
                            throw new Error('invalid overload');
                    };
                    Context.prototype.destroyAllEntities = function () {
                        {
                            var array123 = this.getEntities();
                            for (var index122 = 0; index122 < array123.length; index122++) {
                                var entity = array123[index122];
                                {
                                    this.destroyEntity(entity);
                                }
                            }
                        }
                        this._entities.clear();
                        if (this._retainedEntities.size() !== 0) {
                            throw new ContextStillHasRetainedEntitiesException(this);
                        }
                    };
                    Context.prototype.hasEntity = function (entity) {
                        var _this = this;
                        if (((entity != null) || entity === null)) {
                            var __args = Array.prototype.slice.call(arguments);
                            return (function () {
                                return _this._entities.contains(entity);
                            })();
                        }
                        else
                            throw new Error('invalid overload');
                    };
                    Context.prototype.getEntities$ = function () {
                        if (this._entitiesCache == null) {
                            this._entitiesCache = Array.newInstance(this.entityType, this._entities.size());
                            this._entities.toArray(this._entitiesCache);
                        }
                        return this._entitiesCache;
                    };
                    Context.prototype.getTotalComponents = function () {
                        return this._totalComponents;
                    };
                    Context.prototype.getGroup = function (matcher) {
                        var group = null;
                        if (!(this._groups.containsKey(matcher) ? (group = this._groups.get(matcher)) == null : false)) {
                            group = (new Group(matcher, this.entityType));
                            {
                                var array125 = this.getEntities();
                                for (var index124 = 0; index124 < array125.length; index124++) {
                                    var entity = array125[index124];
                                    {
                                        group.handleEntitySilently(entity);
                                    }
                                }
                            }
                            this._groups.put(matcher, group);
                            {
                                var array127 = matcher.getIndices();
                                for (var index126 = 0; index126 < array127.length; index126++) {
                                    var index = array127[index126];
                                    {
                                        if (this._groupsForIndex[index] == null) {
                                            this._groupsForIndex[index] = Collections.createList(Group);
                                        }
                                        this._groupsForIndex[index].add(group);
                                    }
                                }
                            }
                            this.notifyGroupCreated(group);
                        }
                        return group;
                    };
                    Context.prototype.clearGroups = function () {
                        for (var index128 = this._groups.values().iterator(); index128.hasNext();) {
                            var group = index128.next();
                            {
                                group.removeAllEventHandlers();
                                {
                                    var array130 = group.getEntities();
                                    for (var index129 = 0; index129 < array130.length; index129++) {
                                        var entity = array130[index129];
                                        {
                                            entity.release(group);
                                        }
                                    }
                                }
                                this.notifyGroupCleared(group);
                            }
                        }
                        this._groups.clear();
                        for (var i = 0; i < this._groupsForIndex.length; i++) {
                            this._groupsForIndex[i] = null;
                        }
                    };
                    Context.prototype.addEntityIndex = function (name, entityIndex) {
                        if (this._entityIndices.containsKey(name)) {
                            throw new ContextEntityIndexDoesAlreadyExistException(this, name);
                        }
                        this._entityIndices.put(name, entityIndex);
                    };
                    Context.prototype.getEntityIndex = function (name) {
                        var entityIndex;
                        if (!this._entityIndices.containsKey(name)) {
                            throw new ContextEntityIndexDoesNotExistException(this, name);
                        }
                        else {
                            entityIndex = this._entityIndices.get(name);
                        }
                        return entityIndex;
                    };
                    Context.prototype.deactivateAndRemoveEntityIndices = function () {
                        for (var index131 = this._entityIndices.values().iterator(); index131.hasNext();) {
                            var entityIndex = index131.next();
                            {
                                entityIndex.deactivate();
                            }
                        }
                        this._entityIndices.clear();
                    };
                    Context.prototype.resetCreationIndex = function () {
                        this._creationIndex = 0;
                    };
                    Context.prototype.clearComponentPool = function (index) {
                        var componentPool = this._componentContexts[index];
                        if (componentPool != null) {
                            componentPool.clear();
                        }
                    };
                    Context.prototype.clearComponentPools = function () {
                        for (var i = 0; i < this._componentContexts.length; i++) {
                            this.clearComponentPool(i);
                        }
                    };
                    Context.prototype.reset = function () {
                        this.clearGroups();
                        this.destroyAllEntities();
                        this.resetCreationIndex();
                        this.clearEventsListener();
                    };
                    Context.prototype.updateGroupsComponentAddedOrRemoved = function (entity, index, component, groupsForIndex) {
                        var groups = groupsForIndex[index];
                        if (groups != null) {
                            var events = EntitasCache.getGroupChangedList();
                            for (var i = 0; i < groups.size(); i++) {
                                events.add(groups.get(i).handleEntity(entity));
                            }
                            for (var i = 0; i < events.size(); i++) {
                                var groupChangedEvent = events.get(i);
                                if (groupChangedEvent != null) {
                                    for (var index132 = groupChangedEvent.iterator(); index132.hasNext();) {
                                        var listener = index132.next();
                                        {
                                            listener(groups.get(i), entity, index, component);
                                        }
                                    }
                                }
                            }
                            EntitasCache.pushGroupChangedList(events);
                        }
                    };
                    Context.prototype.updateGroupsComponentReplaced = function (entity, index, previousComponent, newComponent, groupsForIndex) {
                        var groups = groupsForIndex[index];
                        if (groups != null) {
                            for (var index133 = groups.iterator(); index133.hasNext();) {
                                var g = index133.next();
                                {
                                    g.updateEntity(entity, index, previousComponent, newComponent);
                                }
                            }
                        }
                    };
                    Context.prototype.onEntityReleased = function (entity, retainedEntities, reusableEntities) {
                        if (entity.isEnabled()) {
                            throw new EntityIsNotDestroyedException("Cannot release entity.");
                        }
                        entity.removeAllOnEntityReleasedHandlers();
                        retainedEntities.remove(entity);
                        reusableEntities.push(entity);
                    };
                    Context.prototype.getComponentPools = function () {
                        return this._componentContexts;
                    };
                    Context.prototype.getContextInfo = function () {
                        return this._contextInfo;
                    };
                    Context.prototype.getCount = function () {
                        return this._entities.size();
                    };
                    Context.prototype.getReusableEntitiesCount = function () {
                        return this._reusableEntities.size();
                    };
                    Context.prototype.getRetainedEntitiesCount = function () {
                        return this._retainedEntities.size();
                    };
                    Context.prototype.getEntities = function (matcher) {
                        var _this = this;
                        if (((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null)) {
                            var __args = Array.prototype.slice.call(arguments);
                            return (function () {
                                return _this.getGroup(matcher).getEntities();
                            })();
                        }
                        else if (matcher === undefined) {
                            return this.getEntities$();
                        }
                        else
                            throw new Error('invalid overload');
                    };
                    Context.prototype.createCollector$com_ilargia_games_entitas_api_matcher_IMatcher = function (matcher) {
                        return (new Collector(this.getGroup(matcher), GroupEvent.Added));
                    };
                    Context.prototype.createCollector = function (matcher, groupEvent) {
                        var _this = this;
                        if (((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null) && ((typeof groupEvent === 'number') || groupEvent === null)) {
                            var __args = Array.prototype.slice.call(arguments);
                            return (function () {
                                return (new Collector(_this.getGroup(matcher), groupEvent));
                            })();
                        }
                        else if (((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null) && groupEvent === undefined) {
                            return this.createCollector$com_ilargia_games_entitas_api_matcher_IMatcher(matcher);
                        }
                        else
                            throw new Error('invalid overload');
                    };
                    Context.prototype.createEntityCollector$com_ilargia_games_entitas_Context_A$com_ilargia_games_entitas_api_matcher_IMatcher = function (contexts, matcher) {
                        return this.createEntityCollector(contexts, matcher, GroupEvent.Added);
                    };
                    Context.prototype.createEntityCollector = function (contexts, matcher, eventType) {
                        if (((contexts != null && contexts instanceof Array) || contexts === null) && ((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null) && ((typeof eventType === 'number') || eventType === null)) {
                            var __args = Array.prototype.slice.call(arguments);
                            return (function () {
                                var groups = new Array(contexts.length);
                                var eventTypes = new Array(contexts.length);
                                for (var i = 0; i < contexts.length; i++) {
                                    groups[i] = contexts[i].getGroup(matcher);
                                    eventTypes[i] = eventType;
                                }
                                return (new Collector(groups, eventTypes));
                            })();
                        }
                        else if (((contexts != null && contexts instanceof Array) || contexts === null) && ((matcher != null && (matcher["__interfaces"] != null && matcher["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || matcher.constructor != null && matcher.constructor["__interfaces"] != null && matcher.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || matcher === null) && eventType === undefined) {
                            return this.createEntityCollector$com_ilargia_games_entitas_Context_A$com_ilargia_games_entitas_api_matcher_IMatcher(contexts, matcher);
                        }
                        else
                            throw new Error('invalid overload');
                    };
                    Context.prototype.clearEventsListener = function () {
                        if (this.__OnEntityCreated != null)
                            this.__OnEntityCreated.clear();
                        if (this.__OnEntityWillBeDestroyed != null)
                            this.__OnEntityWillBeDestroyed.clear();
                        if (this.__OnEntityDestroyed != null)
                            this.__OnEntityDestroyed.clear();
                        if (this.__OnGroupCreated != null)
                            this.__OnGroupCreated.clear();
                        if (this.__OnGroupCleared != null)
                            this.__OnGroupCleared.clear();
                    };
                    Context.prototype.OnEntityCreated = function (listener) {
                        if (this.__OnEntityCreated != null) {
                            this.__OnEntityCreated = Collections.createSet("com.ilargia.games.entitas.api.events.ContextEntityChanged");
                        }
                        this.__OnEntityCreated.add(listener);
                    };
                    Context.prototype.OnEntityWillBeDestroyed = function (listener) {
                        if (this.__OnEntityWillBeDestroyed != null) {
                            this.__OnEntityWillBeDestroyed = Collections.createSet("com.ilargia.games.entitas.api.events.ContextEntityChanged");
                        }
                        this.__OnEntityWillBeDestroyed.add(listener);
                    };
                    Context.prototype.OnEntityDestroyed = function (listener) {
                        if (this.__OnEntityDestroyed != null) {
                            this.__OnEntityDestroyed = Collections.createSet("com.ilargia.games.entitas.api.events.ContextEntityChanged");
                        }
                        this.__OnEntityDestroyed.add(listener);
                    };
                    Context.prototype.OnGroupCreated = function (listener) {
                        if (this.__OnGroupCreated != null) {
                            this.__OnGroupCreated = Collections.createSet("com.ilargia.games.entitas.api.events.ContextGroupChanged");
                        }
                        this.__OnGroupCreated.add(listener);
                    };
                    Context.prototype.OnGroupCleared = function (listener) {
                        if (this.__OnGroupCleared != null) {
                            this.__OnGroupCleared = Collections.createSet("com.ilargia.games.entitas.api.events.ContextGroupChanged");
                        }
                        this.__OnGroupCleared.add(listener);
                    };
                    Context.prototype.notifyEntityCreated = function (entity) {
                        if (this.__OnEntityCreated != null) {
                            for (var index134 = this.__OnEntityCreated.iterator(); index134.hasNext();) {
                                var listener = index134.next();
                                {
                                    listener(this, entity);
                                }
                            }
                        }
                    };
                    Context.prototype.notifyEntityWillBeDestroyed = function (entity) {
                        if (this.__OnEntityWillBeDestroyed != null) {
                            for (var index135 = this.__OnEntityWillBeDestroyed.iterator(); index135.hasNext();) {
                                var listener = index135.next();
                                {
                                    listener(this, entity);
                                }
                            }
                        }
                    };
                    Context.prototype.notifyEntityDestroyed = function (entity) {
                        if (this.__OnEntityDestroyed != null) {
                            for (var index136 = this.__OnEntityDestroyed.iterator(); index136.hasNext();) {
                                var listener = index136.next();
                                {
                                    listener(this, entity);
                                }
                            }
                        }
                    };
                    Context.prototype.notifyGroupCreated = function (group) {
                        if (this.__OnGroupCreated != null) {
                            for (var index137 = this.__OnGroupCreated.iterator(); index137.hasNext();) {
                                var listener = index137.next();
                                {
                                    listener(this, group);
                                }
                            }
                        }
                    };
                    Context.prototype.notifyGroupCleared = function (group) {
                        if (this.__OnGroupCleared != null) {
                            for (var index138 = this.__OnGroupCleared.iterator(); index138.hasNext();) {
                                var listener = index138.next();
                                {
                                    listener(this, group);
                                }
                            }
                        }
                    };
                    Context.prototype.equals = function (o) {
                        if (this === o)
                            return true;
                        if (o == null || this.constructor !== o.constructor)
                            return false;
                        var context = o;
                        if (this._totalComponents !== context._totalComponents)
                            return false;
                        if (this.entityType != null ? !this.entityType.equals(context.entityType) : context.entityType != null)
                            return false;
                        return this._contextInfo != null ? this._contextInfo.equals(context._contextInfo) : context._contextInfo == null;
                    };
                    Context.prototype.hashCode = function () {
                        var result = 0;
                        result = 31 * result + this._totalComponents;
                        result = 31 * result + (this.entityType != null ? this.entityType.hashCode() : 0);
                        result = 31 * result + (this._contextInfo != null ? this._contextInfo.hashCode() : 0);
                        return result;
                    };
                    Context.prototype.toString = function () {
                        return "Context{_totalComponents=" + this._totalComponents + ", entityType=" + this.entityType + ", _groups=" + this._groups + ", _groupsForIndex=" + Arrays.toString(this._groupsForIndex) + ", _creationIndex=" + this._creationIndex + ", _entities=" + this._entities + ", _reusableEntities=" + this._reusableEntities + ", _retainedEntities=" + this._retainedEntities + ", _entitiesCache=" + Arrays.toString(this._entitiesCache) + ", _entityIndices=" + this._entityIndices + ", _factoryEntiy=" + this._factoryEntiy + ", _contextInfo=" + this._contextInfo + ", _componentContexts=" + Arrays.toString(this._componentContexts) + ", _cachedEntityChanged=" + this._cachedEntityChanged + '}';
                    };
                    return Context;
                }());
                entitas.Context = Context;
                Context["__class"] = "com.ilargia.games.entitas.Context";
                Context["__interfaces"] = ["com.ilargia.games.entitas.api.IContext"];
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
