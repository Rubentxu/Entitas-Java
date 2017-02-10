/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var api;
                (function (api) {
                    var events;
                    (function (events) {
                        var Collections = com.ilargia.games.entitas.factories.Collections;
                        var EventMap = (function () {
                            function EventMap() {
                                this.__listeners = Collections.createMap(Object, Object);
                            }
                            EventMap.prototype.removeListener = function (key) {
                                return this.__listeners.remove(key);
                            };
                            EventMap.prototype.addListener = function (key, eventHandler) {
                                this.__listeners.put(key, eventHandler);
                                return eventHandler;
                            };
                            EventMap.prototype.listeners = function () {
                                return this.__listeners.values();
                            };
                            EventMap.prototype.getEventHandler = function (key) {
                                return this.__listeners.get(key);
                            };
                            EventMap.prototype.clear = function () {
                                this.__listeners.clear();
                            };
                            return EventMap;
                        }());
                        events.EventMap = EventMap;
                        EventMap["__class"] = "com.ilargia.games.entitas.api.events.EventMap";
                    })(events = api.events || (api.events = {}));
                })(api = entitas.api || (entitas.api = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
