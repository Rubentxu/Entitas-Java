/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var collector;
                (function (collector) {
                    var EntityCollectorException = com.ilargia.games.entitas.exceptions.EntityCollectorException;
                    var Collections = com.ilargia.games.entitas.factories.Collections;
                    var Arrays = java.util.Arrays;
                    var Collector = (function () {
                        function Collector(group, eventType) {
                            var _this = this;
                            if (((group != null && (group["__interfaces"] != null && group["__interfaces"].indexOf("com.ilargia.games.entitas.api.IGroup") >= 0 || group.constructor != null && group.constructor["__interfaces"] != null && group.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.IGroup") >= 0)) || group === null) && ((typeof eventType === 'number') || eventType === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                {
                                    var __args_1 = Array.prototype.slice.call(arguments);
                                    var groups_1 = [group];
                                    var groupEvents_1 = [eventType];
                                    (function () {
                                        _this._groups = groups_1;
                                        _this._collectedEntities = Collections.createSet("com.ilargia.games.entitas.api.IEntity");
                                        _this._groupEvents = groupEvents_1;
                                        if (groups_1.length !== groupEvents_1.length) {
                                            throw new EntityCollectorException("Unbalanced count with groups (" + groups_1.length + ") and event types (" + groupEvents_1.length + ").", "Group and event type count must be equal.");
                                        }
                                        _this._addEntityCache = function (group, entity, index, component) {
                                            _this.addEntity(group, entity, index, component);
                                        };
                                        _this.activate();
                                    })();
                                }
                            }
                            else if (((group != null && group instanceof Array) || group === null) && ((eventType != null && eventType instanceof Array) || eventType === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                var groups_2 = __args[0];
                                var groupEvents_2 = __args[1];
                                (function () {
                                    _this._groups = groups_2;
                                    _this._collectedEntities = Collections.createSet("com.ilargia.games.entitas.api.IEntity");
                                    _this._groupEvents = groupEvents_2;
                                    if (groups_2.length !== groupEvents_2.length) {
                                        throw new EntityCollectorException("Unbalanced count with groups (" + groups_2.length + ") and event types (" + groupEvents_2.length + ").", "Group and event type count must be equal.");
                                    }
                                    _this._addEntityCache = function (group, entity, index, component) {
                                        _this.addEntity(group, entity, index, component);
                                    };
                                    _this.activate();
                                })();
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        Collector.prototype.activate = function () {
                            for (var i = 0; i < this._groups.length; i++) {
                                var group = this._groups[i];
                                var groupEvent = this._groupEvents[i];
                                switch ((groupEvent)) {
                                    case com.ilargia.games.entitas.events.GroupEvent.Added:
                                        group.OnEntityAdded(this._addEntityCache);
                                        break;
                                    case com.ilargia.games.entitas.events.GroupEvent.Removed:
                                        group.OnEntityRemoved(this._addEntityCache);
                                        break;
                                    case com.ilargia.games.entitas.events.GroupEvent.AddedOrRemoved:
                                        group.OnEntityAdded(this._addEntityCache);
                                        group.OnEntityRemoved(this._addEntityCache);
                                        break;
                                }
                            }
                        };
                        Collector.prototype.deactivate = function () {
                            for (var i = 0; i < this._groups.length; i++) {
                                var group = this._groups[i];
                                group.__OnEntityAdded.clear();
                                group.__OnEntityRemoved.clear();
                            }
                            this.clearCollectedEntities();
                        };
                        Collector.prototype.clearCollectedEntities = function () {
                            for (var index121 = this._collectedEntities.iterator(); index121.hasNext();) {
                                var entity = index121.next();
                                {
                                    entity.release(this);
                                }
                            }
                            this._collectedEntities.clear();
                        };
                        Collector.prototype.addEntity = function (group, entity, index, component) {
                            var added = this._collectedEntities.add(entity);
                            if (added) {
                                entity.retain(this);
                            }
                        };
                        Collector.prototype.toString = function () {
                            return "Collector{_collectedEntities=" + this._collectedEntities + ", _groups=" + Arrays.toString(this._groups) + ", _groupEvents=" + Arrays.toString(this._groupEvents) + ", _addEntityCache=" + this._addEntityCache + ", _toStringCache=\'" + this._toStringCache + '\'' + ", _toStringBuilder=" + this._toStringBuilder + '}';
                        };
                        return Collector;
                    }());
                    collector.Collector = Collector;
                    Collector["__class"] = "com.ilargia.games.entitas.collector.Collector";
                })(collector = entitas.collector || (entitas.collector = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
