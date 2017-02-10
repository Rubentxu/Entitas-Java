/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var systems;
                (function (systems) {
                    var Entity = com.ilargia.games.entitas.Entity;
                    var Collections = com.ilargia.games.entitas.factories.Collections;
                    var ReactiveSystem = (function () {
                        function ReactiveSystem(context) {
                            var _this = this;
                            if (((context != null && (context["__interfaces"] != null && context["__interfaces"].indexOf("com.ilargia.games.entitas.api.IContext") >= 0 || context.constructor != null && context.constructor["__interfaces"] != null && context.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.IContext") >= 0)) || context === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                (function () {
                                    _this._collector = _this.getTrigger(context);
                                    _this._buffer = Collections.createList(Entity);
                                })();
                            }
                            else if (((context != null && context instanceof com.ilargia.games.entitas.collector.Collector) || context === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                var collector_1 = __args[0];
                                (function () {
                                    _this._collector = collector_1;
                                    _this._buffer = Collections.createList(Entity);
                                })();
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        ReactiveSystem.prototype.execute = function (entities) {
                            if (((entities != null && (entities["__interfaces"] != null && entities["__interfaces"].indexOf("java.util.List") >= 0 || entities.constructor != null && entities.constructor["__interfaces"] != null && entities.constructor["__interfaces"].indexOf("java.util.List") >= 0)) || entities === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                            }
                            else if (((typeof entities === 'number') || entities === null)) {
                                return this.execute$float(entities);
                            }
                            else
                                throw new Error('invalid overload');
                        };
                        ReactiveSystem.prototype.activate = function () {
                            this._collector.activate();
                        };
                        ReactiveSystem.prototype.deactivate = function () {
                            this._collector.deactivate();
                        };
                        ReactiveSystem.prototype.clear = function () {
                            this._collector.clearCollectedEntities();
                        };
                        ReactiveSystem.prototype.execute$float = function (deltatime) {
                            if (this._collector._collectedEntities.size() !== 0) {
                                for (var index157 = this._collector._collectedEntities.iterator(); index157.hasNext();) {
                                    var e = index157.next();
                                    {
                                        if (this.filter(e)) {
                                            e.retain(this);
                                            this._buffer.add(e);
                                        }
                                    }
                                }
                                this._collector.clearCollectedEntities();
                                if (this._buffer.size() !== 0) {
                                    this.execute(this._buffer);
                                    for (var i = 0; i < this._buffer.size(); i++) {
                                        this._buffer.get(i).release(this);
                                    }
                                    this._buffer.clear();
                                }
                            }
                        };
                        ReactiveSystem.prototype.toString = function () {
                            return "ReactiveSystem{_collector=" + this._collector + ", _buffer=" + this._buffer + ", _toStringCache=\'" + this._toStringCache + '\'' + '}';
                        };
                        return ReactiveSystem;
                    }());
                    systems.ReactiveSystem = ReactiveSystem;
                    ReactiveSystem["__class"] = "com.ilargia.games.entitas.systems.ReactiveSystem";
                    ReactiveSystem["__interfaces"] = ["com.ilargia.games.entitas.api.system.IReactiveSystem", "com.ilargia.games.entitas.api.system.ISystem", "com.ilargia.games.entitas.api.system.IExecuteSystem"];
                })(systems = entitas.systems || (entitas.systems = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
