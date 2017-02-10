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
                    var SingleEntityException = (function (_super) {
                        __extends(SingleEntityException, _super);
                        function SingleEntityException(count) {
                            _super.call(this, "Expected exactly one entity in collection but found " + count + "!", "Use collection.SingleEntity() only when you are sure that there is exactly one entity.");
                        }
                        return SingleEntityException;
                    }(EntitasException));
                    exceptions.SingleEntityException = SingleEntityException;
                    SingleEntityException["__class"] = "com.ilargia.games.entitas.exceptions.SingleEntityException";
                    SingleEntityException["__interfaces"] = ["java.io.Serializable"];
                })(exceptions = entitas.exceptions || (entitas.exceptions = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
