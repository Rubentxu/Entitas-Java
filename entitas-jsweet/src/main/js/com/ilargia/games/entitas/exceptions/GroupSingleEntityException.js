var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var exceptions;
                (function (exceptions) {
                    var EntitasException = com.ilargia.games.entitas.api.EntitasException;
                    var GroupSingleEntityException = (function (_super) {
                        __extends(GroupSingleEntityException, _super);
                        function GroupSingleEntityException(group) {
                            _super.call(this, "Cannot get the single entity from " + group + "!\nGroup contains " + group.getCount() + " entities:", "");
                        }
                        return GroupSingleEntityException;
                    }(EntitasException));
                    exceptions.GroupSingleEntityException = GroupSingleEntityException;
                    GroupSingleEntityException["__class"] = "com.ilargia.games.entitas.exceptions.GroupSingleEntityException";
                    GroupSingleEntityException["__interfaces"] = ["java.io.Serializable"];
                })(exceptions = entitas.exceptions || (entitas.exceptions = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
