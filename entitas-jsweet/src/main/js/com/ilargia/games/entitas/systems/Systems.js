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
                    var Collections = com.ilargia.games.entitas.factories.Collections;
                    var Systems = (function () {
                        function Systems() {
                            this._initializeSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
                            this._executeSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
                            this._renderSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
                            this._cleanupSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
                            this._tearDownSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
                        }
                        Systems.prototype.add = function (system) {
                            if (system != null) {
                                if (system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IInitializeSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IInitializeSystem") >= 0))
                                    this._initializeSystems.add(system);
                                if (system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IExecuteSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IExecuteSystem") >= 0))
                                    this._executeSystems.add(system);
                                if (system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IRenderSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IRenderSystem") >= 0))
                                    this._renderSystems.add(system);
                                if (system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.ICleanupSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.ICleanupSystem") >= 0))
                                    this._cleanupSystems.add(system);
                                if (system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.ITearDownSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.ITearDownSystem") >= 0))
                                    this._tearDownSystems.add(system);
                            }
                            return this;
                        };
                        Systems.prototype.initialize = function () {
                            for (var index158 = this._initializeSystems.iterator(); index158.hasNext();) {
                                var iSystem = index158.next();
                                {
                                    iSystem.initialize();
                                }
                            }
                        };
                        Systems.prototype.execute = function (entities) {
                            if (((typeof entities === 'number') || entities === null)) {
                                return this.execute$float(entities);
                            }
                            else
                                throw new Error('invalid overload');
                        };
                        Systems.prototype.execute$float = function (deltaTime) {
                            for (var index159 = this._executeSystems.iterator(); index159.hasNext();) {
                                var eSystem = index159.next();
                                {
                                    eSystem.execute(deltaTime);
                                }
                            }
                        };
                        Systems.prototype.render = function () {
                            for (var index160 = this._renderSystems.iterator(); index160.hasNext();) {
                                var eSystem = index160.next();
                                {
                                    eSystem.render();
                                }
                            }
                        };
                        Systems.prototype.cleanup = function () {
                            for (var index161 = this._cleanupSystems.iterator(); index161.hasNext();) {
                                var clSystem = index161.next();
                                {
                                    clSystem.cleanup();
                                }
                            }
                        };
                        Systems.prototype.tearDown = function () {
                            for (var index162 = this._tearDownSystems.iterator(); index162.hasNext();) {
                                var tSystem = index162.next();
                                {
                                    tSystem.tearDown();
                                }
                            }
                        };
                        Systems.prototype.activateReactiveSystems = function () {
                            for (var i = 0; i < this._executeSystems.size(); i++) {
                                var reactiveSystem = ((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.ReactiveSystem) ? this._executeSystems.get(i) : null);
                                if (reactiveSystem != null) {
                                    reactiveSystem.activate();
                                }
                                var nestedSystems = ((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.Systems) ? this._executeSystems.get(i) : null);
                                if (nestedSystems != null) {
                                    nestedSystems.activateReactiveSystems();
                                }
                            }
                        };
                        Systems.prototype.deactivateReactiveSystems = function () {
                            for (var i = 0; i < this._executeSystems.size(); i++) {
                                var reactiveSystem = ((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.ReactiveSystem) ? this._executeSystems.get(i) : null);
                                if (reactiveSystem != null) {
                                    reactiveSystem.deactivate();
                                }
                                var nestedSystems = ((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.Systems) ? this._executeSystems.get(i) : null);
                                if (nestedSystems != null) {
                                    nestedSystems.deactivateReactiveSystems();
                                }
                            }
                        };
                        Systems.prototype.clearReactiveSystems = function () {
                            for (var i = 0; i < this._executeSystems.size(); i++) {
                                var reactiveSystem = ((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.ReactiveSystem) ? this._executeSystems.get(i) : null);
                                if (reactiveSystem != null) {
                                    reactiveSystem.clear();
                                }
                                var nestedSystems = ((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.Systems) ? this._executeSystems.get(i) : null);
                                if (nestedSystems != null) {
                                    nestedSystems.clearReactiveSystems();
                                }
                            }
                        };
                        Systems.prototype.clearSystems = function () {
                            this._initializeSystems.clear();
                            this._executeSystems.clear();
                            this._renderSystems.clear();
                            this._cleanupSystems.clear();
                            this._tearDownSystems.clear();
                        };
                        Systems.prototype.toString = function () {
                            return "Systems{_initializeSystems=" + this._initializeSystems + ", _executeSystems=" + this._executeSystems + ", _renderSystems=" + this._renderSystems + ", _cleanupSystems=" + this._cleanupSystems + ", _tearDownSystems=" + this._tearDownSystems + '}';
                        };
                        return Systems;
                    }());
                    systems.Systems = Systems;
                    Systems["__class"] = "com.ilargia.games.entitas.systems.Systems";
                    Systems["__interfaces"] = ["com.ilargia.games.entitas.api.system.ICleanupSystem", "com.ilargia.games.entitas.api.system.ISystem", "com.ilargia.games.entitas.api.system.IRenderSystem", "com.ilargia.games.entitas.api.system.IInitializeSystem", "com.ilargia.games.entitas.api.system.IExecuteSystem", "com.ilargia.games.entitas.api.system.ITearDownSystem"];
                })(systems = entitas.systems || (entitas.systems = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
