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
                    var ContextInfoException = (function (_super) {
                        __extends(ContextInfoException, _super);
                        function ContextInfoException(pool, contextInfo) {
                            _super.call(this, "Invalid ContextInfo for \'" + pool + "\'!\nExpected " + pool._totalComponents + " componentName(s) but got " + contextInfo.componentNames.length + ":", "");
                        }
                        return ContextInfoException;
                    }(EntitasException));
                    exceptions.ContextInfoException = ContextInfoException;
                    ContextInfoException["__class"] = "com.ilargia.games.entitas.exceptions.ContextInfoException";
                    ContextInfoException["__interfaces"] = ["java.io.Serializable"];
                })(exceptions = entitas.exceptions || (entitas.exceptions = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
