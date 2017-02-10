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
                    var Arrays = java.util.Arrays;
                    var ContextInfo = (function () {
                        function ContextInfo(contextName, componentNames, componentTypes) {
                            this.contextName = contextName;
                            this.componentNames = componentNames;
                            this.componentTypes = componentTypes;
                        }
                        ContextInfo.prototype.equals = function (o) {
                            if (this === o)
                                return true;
                            if (!(o != null && o instanceof com.ilargia.games.entitas.api.ContextInfo))
                                return false;
                            var that = o;
                            if (this.contextName != null ? !(this.contextName === that.contextName) : that.contextName != null)
                                return false;
                            if (!Arrays.equals(this.componentNames, that.componentNames))
                                return false;
                            return Arrays.equals(this.componentTypes, that.componentTypes);
                        };
                        ContextInfo.prototype.hashCode = function () {
                            var result = this.contextName != null ? this.contextName.toString() : 0;
                            result = 31 * result + Arrays.hashCode(this.componentNames);
                            result = 31 * result + Arrays.hashCode(this.componentTypes);
                            return result;
                        };
                        ContextInfo.prototype.toString = function () {
                            return "ContextInfo{contextName=\'" + this.contextName + '\'' + ", componentNames=" + Arrays.toString(this.componentNames) + '}';
                        };
                        return ContextInfo;
                    }());
                    api.ContextInfo = ContextInfo;
                    ContextInfo["__class"] = "com.ilargia.games.entitas.api.ContextInfo";
                })(api = entitas.api || (entitas.api = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
