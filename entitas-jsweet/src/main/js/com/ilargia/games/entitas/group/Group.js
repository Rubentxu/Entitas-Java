/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var group;
                (function (group_1) {
                    var Entity = com.ilargia.games.entitas.Entity;
                    var Collector = com.ilargia.games.entitas.collector.Collector;
                    var GroupSingleEntityException = com.ilargia.games.entitas.exceptions.GroupSingleEntityException;
                    var Collections = com.ilargia.games.entitas.factories.Collections;
                    var Group = (function () {
                        function Group(matcher, clazz) {
                            this.__OnEntityAdded = Collections.createSet("com.ilargia.games.entitas.api.events.GroupChanged");
                            this.__OnEntityRemoved = Collections.createSet("com.ilargia.games.entitas.api.events.GroupChanged");
                            this.__OnEntityUpdated = Collections.createSet("com.ilargia.games.entitas.api.events.GroupUpdated");
                            this._entities = Collections.createSet(Entity);
                            this._matcher = matcher;
                            this.type = clazz;
                        }
                        Group.createCollector = function (group, groupEvent) {
                            return (new Collector(group, groupEvent));
                        };
                        Group.prototype.getCount = function () {
                            return this._entities.size();
                        };
                        Group.prototype.getMatcher = function () {
                            return this._matcher;
                        };
                        Group.prototype.handleEntitySilently = function (entity) {
                            if (this._matcher.matches(entity)) {
                                this.addEntitySilently(entity);
                            }
                            else {
                                this.removeEntitySilently(entity);
                            }
                        };
                        Group.prototype.handleEntity = function (entity, index, component) {
                            var _this = this;
                            if (((entity != null) || entity === null) && ((typeof index === 'number') || index === null) && ((component != null && (component["__interfaces"] != null && component["__interfaces"].indexOf("com.ilargia.games.entitas.api.IComponent") >= 0 || component.constructor != null && component.constructor["__interfaces"] != null && component.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.IComponent") >= 0)) || component === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                return (function () {
                                    if (_this._matcher.matches(entity)) {
                                        _this.addEntitySilently(entity);
                                    }
                                    else {
                                        _this.removeEntitySilently(entity);
                                    }
                                })();
                            }
                            else if (((entity != null) || entity === null) && index === undefined && component === undefined) {
                                return this.handleEntity$com_ilargia_games_entitas_api_IEntity(entity);
                            }
                            else
                                throw new Error('invalid overload');
                        };
                        Group.prototype.updateEntity = function (entity, index, previousComponent, newComponent) {
                            if (this._entities.contains(entity)) {
                                this.notifyOnEntityRemoved(entity, index, previousComponent);
                                this.notifyOnEntityAdded(entity, index, previousComponent);
                                this.notifyOnEntityUpdated(entity, index, previousComponent, newComponent);
                            }
                        };
                        Group.prototype.removeAllEventHandlers = function () {
                            this.__OnEntityAdded.clear();
                            this.__OnEntityRemoved.clear();
                            this.__OnEntityUpdated.clear();
                        };
                        Group.prototype.handleEntity$com_ilargia_games_entitas_api_IEntity = function (entity) {
                            return (this._matcher.matches(entity)) ? (this.addEntitySilently(entity)) ? this.__OnEntityAdded : null : (this.removeEntitySilently(entity)) ? this.__OnEntityRemoved : null;
                        };
                        Group.prototype.addEntitySilently = function (entity) {
                            var added = this._entities.add(entity);
                            if (added) {
                                this._entitiesCache = null;
                                this._singleEntityCache = null;
                                entity.retain(this);
                            }
                            return added;
                        };
                        Group.prototype.addEntity = function (entity, index, component) {
                            if (this.addEntitySilently(entity)) {
                                this.notifyOnEntityAdded(entity, index, component);
                            }
                        };
                        Group.prototype.removeEntitySilently = function (entity) {
                            var removed = this._entities.remove(entity);
                            if (removed) {
                                this._entitiesCache = null;
                                this._singleEntityCache = null;
                                entity.release(this);
                            }
                            return removed;
                        };
                        Group.prototype.removeEntity = function (entity, index, component) {
                            var removed = this._entities.remove(entity);
                            if (removed) {
                                this._entitiesCache = null;
                                this._singleEntityCache = null;
                                this.notifyOnEntityRemoved(entity, index, component);
                                entity.release(this);
                            }
                        };
                        Group.prototype.containsEntity = function (entity) {
                            return this._entities.contains(entity);
                        };
                        Group.prototype.getEntities = function () {
                            if (this._entitiesCache == null) {
                                this._entitiesCache = java.lang.reflect.Array.newInstance(this.type, this._entities.size());
                                var i = 0;
                                for (var index144 = this._entities.iterator(); index144.hasNext();) {
                                    var entity = index144.next();
                                    {
                                        this._entitiesCache[i] = entity;
                                        i++;
                                    }
                                }
                            }
                            return this._entitiesCache;
                        };
                        Group.prototype.getSingleEntity = function () {
                            if (this._singleEntityCache == null) {
                                var c = this._entities.size();
                                if (c === 1) {
                                    var enumerator = this._entities.iterator();
                                    this._singleEntityCache = enumerator.next();
                                }
                                else if (c === 0) {
                                    return null;
                                }
                                else {
                                    throw new GroupSingleEntityException(this);
                                }
                            }
                            return this._singleEntityCache;
                        };
                        Group.prototype.equals = function (o) {
                            if (this === o)
                                return true;
                            if (!(o != null && o instanceof com.ilargia.games.entitas.group.Group))
                                return false;
                            var group = o;
                            if (this._matcher != null ? !this._matcher.equals(group._matcher) : group._matcher != null)
                                return false;
                            if (this._entities != null ? !this._entities.equals(group._entities) : group._entities != null)
                                return false;
                            return this.type != null ? this.type.equals(group.type) : group.type == null;
                        };
                        Group.prototype.hashCode = function () {
                            var result = this._matcher != null ? this._matcher.hashCode() : 0;
                            result = 31 * result + (this.type != null ? this.type.hashCode() : 0);
                            return result;
                        };
                        Group.prototype.toString = function () {
                            return "Group{_matcher=" + this._matcher + ", _entities=" + this._entities + ", _singleEntityCache=" + this._singleEntityCache + ", type=" + this.type + '}';
                        };
                        Group.prototype.clearEventsListener = function () {
                            if (this.__OnEntityAdded != null)
                                this.__OnEntityAdded.clear();
                            if (this.__OnEntityRemoved != null)
                                this.__OnEntityRemoved.clear();
                            if (this.__OnEntityUpdated != null)
                                this.__OnEntityUpdated.clear();
                        };
                        Group.prototype.OnEntityAdded = function (listener) {
                            if (this.__OnEntityAdded != null) {
                                this.__OnEntityAdded = Collections.createSet("com.ilargia.games.entitas.api.events.GroupChanged");
                            }
                            this.__OnEntityAdded.add(listener);
                        };
                        Group.prototype.OnEntityUpdated = function (listener) {
                            if (this.__OnEntityUpdated != null) {
                                this.__OnEntityUpdated = Collections.createSet("com.ilargia.games.entitas.api.events.GroupUpdated");
                            }
                            this.__OnEntityUpdated.add(listener);
                        };
                        Group.prototype.OnEntityRemoved = function (listener) {
                            if (this.__OnEntityRemoved != null) {
                                this.__OnEntityRemoved = Collections.createSet("com.ilargia.games.entitas.api.events.GroupChanged");
                            }
                            this.__OnEntityRemoved.add(listener);
                        };
                        Group.prototype.notifyOnEntityAdded = function (entity, index, component) {
                            if (this.__OnEntityAdded != null) {
                                for (var index145 = this.__OnEntityAdded.iterator(); index145.hasNext();) {
                                    var listener = index145.next();
                                    {
                                        listener(this, entity, index, component);
                                    }
                                }
                            }
                        };
                        Group.prototype.notifyOnEntityUpdated = function (entity, index, component, newComponent) {
                            if (this.__OnEntityUpdated != null) {
                                for (var index146 = this.__OnEntityUpdated.iterator(); index146.hasNext();) {
                                    var listener = index146.next();
                                    {
                                        listener(this, entity, index, component, newComponent);
                                    }
                                }
                            }
                        };
                        Group.prototype.notifyOnEntityRemoved = function (entity, index, component) {
                            if (this.__OnEntityRemoved != null) {
                                for (var index147 = this.__OnEntityRemoved.iterator(); index147.hasNext();) {
                                    var listener = index147.next();
                                    {
                                        listener(this, entity, index, component);
                                    }
                                }
                            }
                        };
                        return Group;
                    }());
                    group_1.Group = Group;
                    Group["__class"] = "com.ilargia.games.entitas.group.Group";
                    Group["__interfaces"] = ["com.ilargia.games.entitas.api.IGroup"];
                })(group = entitas.group || (entitas.group = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
