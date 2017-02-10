/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var matcher;
                (function (matcher) {
                    var TriggerOnEvent = (function () {
                        function TriggerOnEvent(trigger, eventType) {
                            var _this = this;
                            if (((trigger != null && (trigger["__interfaces"] != null && trigger["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || trigger === null) && ((typeof eventType === 'number') || eventType === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                (function () {
                                    _this.trigger = trigger;
                                    _this.eventType = eventType;
                                })();
                            }
                            else if (trigger === undefined && eventType === undefined) {
                                var __args = Array.prototype.slice.call(arguments);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        TriggerOnEvent.prototype.clone = function () {
                            var varCopy = new TriggerOnEvent();
                            varCopy.trigger = this.trigger;
                            varCopy.eventType = this.eventType;
                            return varCopy;
                        };
                        return TriggerOnEvent;
                    }());
                    matcher.TriggerOnEvent = TriggerOnEvent;
                    TriggerOnEvent["__class"] = "com.ilargia.games.entitas.matcher.TriggerOnEvent";
                })(matcher = entitas.matcher || (entitas.matcher = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
