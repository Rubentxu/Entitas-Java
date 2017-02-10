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
                        var Event = (function () {
                            function Event() {
                            }
                            Event.prototype.removeListener = function (eventHandler) {
                                return this.__listeners.remove(eventHandler);
                            };
                            Event.prototype.addListener = function (eventHandler) {
                                this.__listeners.add(eventHandler);
                                return eventHandler;
                            };
                            Event.prototype.listeners = function () {
                                return this.__listeners;
                            };
                            Event.prototype.clear = function () {
                                this.__listeners.clear();
                            };
                            return Event;
                        }());
                        events.Event = Event;
                        Event["__class"] = "com.ilargia.games.entitas.api.events.Event";
                    })(events = api.events || (api.events = {}));
                })(api = entitas.api || (entitas.api = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
