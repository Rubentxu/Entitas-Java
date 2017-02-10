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
                    var Collections = com.ilargia.games.entitas.factories.Collections;
                    var EntitasCache = (function () {
                        function EntitasCache() {
                        }
                        EntitasCache.componentArray_$LI$ = function () {
                            if (EntitasCache.componentArray == null)
                                EntitasCache.componentArray = new ObjectPool(function () {
                                    return Collections.createList("com.ilargia.games.entitas.api.IComponent");
                                }, null);
                            return EntitasCache.componentArray;
                        };
                        ;
                        EntitasCache.integerArray_$LI$ = function () {
                            if (EntitasCache.integerArray == null)
                                EntitasCache.integerArray = new ObjectPool(function () {
                                    return Collections.createList(Number);
                                }, null);
                            return EntitasCache.integerArray;
                        };
                        ;
                        EntitasCache.integerSet_$LI$ = function () {
                            if (EntitasCache.integerSet == null)
                                EntitasCache.integerSet = new ObjectPool(function () {
                                    return Collections.createSet(Number);
                                }, null);
                            return EntitasCache.integerSet;
                        };
                        ;
                        EntitasCache.groupChangedArray_$LI$ = function () {
                            if (EntitasCache.groupChangedArray == null)
                                EntitasCache.groupChangedArray = new ObjectPool(function () {
                                    return Collections.createList("com.ilargia.games.entitas.api.events.GroupChanged");
                                }, null);
                            return EntitasCache.groupChangedArray;
                        };
                        ;
                        EntitasCache.getIComponentList = function () {
                            return EntitasCache.componentArray_$LI$().get();
                        };
                        EntitasCache.pushIComponentList = function (list) {
                            list.clear();
                            EntitasCache.componentArray_$LI$().push(list);
                        };
                        EntitasCache.getIntArray = function () {
                            return EntitasCache.integerArray_$LI$().get();
                        };
                        EntitasCache.pushIntArray = function (list) {
                            list.clear();
                            EntitasCache.integerArray_$LI$().push(list);
                        };
                        EntitasCache.getIntHashSet = function () {
                            return EntitasCache.integerSet_$LI$().get();
                        };
                        EntitasCache.pushIntHashSet = function (hashSet) {
                            hashSet.clear();
                            EntitasCache.integerSet_$LI$().push(hashSet);
                        };
                        EntitasCache.getGroupChangedList = function () {
                            return EntitasCache.groupChangedArray_$LI$().get();
                        };
                        EntitasCache.pushGroupChangedList = function (list) {
                            list.clear();
                            EntitasCache.groupChangedArray_$LI$().push(list);
                        };
                        EntitasCache.reset = function () {
                            EntitasCache.componentArray_$LI$().reset();
                            EntitasCache.integerArray_$LI$().reset();
                            EntitasCache.integerSet_$LI$().reset();
                            EntitasCache.groupChangedArray_$LI$().reset();
                        };
                        return EntitasCache;
                    }());
                    caching.EntitasCache = EntitasCache;
                    EntitasCache["__class"] = "com.ilargia.games.entitas.caching.EntitasCache";
                })(caching = entitas.caching || (entitas.caching = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
com.ilargia.games.entitas.caching.EntitasCache.groupChangedArray_$LI$();
com.ilargia.games.entitas.caching.EntitasCache.integerSet_$LI$();
com.ilargia.games.entitas.caching.EntitasCache.integerArray_$LI$();
com.ilargia.games.entitas.caching.EntitasCache.componentArray_$LI$();
