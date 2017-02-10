/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var caching;
                (function (caching) {
                    var Stack = java.util.Stack;
                    var ObjectPool = (function () {
                        function ObjectPool(factoryMethod, resetMethod) {
                            var _this = this;
                            if (((factoryMethod != null && (factoryMethod["__interfaces"] != null && factoryMethod["__interfaces"].indexOf("com.ilargia.games.entitas.caching.ObjectPool.Factory") >= 0 || factoryMethod.constructor != null && factoryMethod.constructor["__interfaces"] != null && factoryMethod.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.caching.ObjectPool.Factory") >= 0)) || factoryMethod === null) && ((typeof resetMethod === 'function' && resetMethod.length == 1) || resetMethod === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                (function () {
                                    _this._factoryMethod = factoryMethod;
                                    _this._resetMethod = resetMethod;
                                    _this._pool = (new Stack());
                                })();
                            }
                            else if (factoryMethod === undefined && resetMethod === undefined) {
                                var __args = Array.prototype.slice.call(arguments);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        ObjectPool.prototype.get = function () {
                            return this._pool.size() === 0 ? this._factoryMethod.create() : this._pool.pop();
                        };
                        ObjectPool.prototype.push = function (obj) {
                            if (this._resetMethod != null) {
                                this._resetMethod(obj);
                            }
                            this._pool.push(obj);
                        };
                        ObjectPool.prototype.reset = function () {
                            this._pool.clear();
                        };
                        return ObjectPool;
                    }());
                    caching.ObjectPool = ObjectPool;
                    ObjectPool["__class"] = "com.ilargia.games.entitas.caching.ObjectPool";
                })(caching = entitas.caching || (entitas.caching = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
