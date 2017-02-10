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
                    var EntityIsNotRetainedByOwnerException = (function (_super) {
                        __extends(EntityIsNotRetainedByOwnerException, _super);
                        function EntityIsNotRetainedByOwnerException(owner) {
                            _super.call(this, "Entity is not retained by owner: " + owner);
                            this.message = "Entity is not retained by owner: " + owner;
                        }
                        return EntityIsNotRetainedByOwnerException;
                    }(Error));
                    exceptions.EntityIsNotRetainedByOwnerException = EntityIsNotRetainedByOwnerException;
                    EntityIsNotRetainedByOwnerException["__class"] = "com.ilargia.games.entitas.exceptions.EntityIsNotRetainedByOwnerException";
                    EntityIsNotRetainedByOwnerException["__interfaces"] = ["java.io.Serializable"];
                })(exceptions = entitas.exceptions || (entitas.exceptions = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
