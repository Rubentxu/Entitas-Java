/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var events;
                (function (events) {
                    (function (GroupEvent) {
                        GroupEvent[GroupEvent["Added"] = 0] = "Added";
                        GroupEvent[GroupEvent["Removed"] = 1] = "Removed";
                        GroupEvent[GroupEvent["AddedOrRemoved"] = 2] = "AddedOrRemoved";
                    })(events.GroupEvent || (events.GroupEvent = {}));
                    var GroupEvent = events.GroupEvent;
                    var GroupEvent_$WRAPPER = (function () {
                        function GroupEvent_$WRAPPER() {
                        }
                        GroupEvent_$WRAPPER.forValue = function (value) {
                            return GroupEvent_$WRAPPER.values()[value];
                        };
                        GroupEvent_$WRAPPER.prototype.getValue = function () {
                            return this.ordinal();
                        };
                        GroupEvent_$WRAPPER.prototype.name = function () { return this._$name; };
                        GroupEvent_$WRAPPER.prototype.ordinal = function () { return this._$ordinal; };
                        return GroupEvent_$WRAPPER;
                    }());
                    events.GroupEvent_$WRAPPER = GroupEvent_$WRAPPER;
                    GroupEvent["__class"] = "com.ilargia.games.entitas.events.GroupEvent";
                    GroupEvent["__interfaces"] = ["java.lang.Comparable", "java.io.Serializable"];
                    GroupEvent["_$wrappers"] = [new GroupEvent_$WRAPPER(0, "Added"), new GroupEvent_$WRAPPER(1, "Removed"), new GroupEvent_$WRAPPER(2, "AddedOrRemoved")];
                })(events = entitas.events || (entitas.events = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
