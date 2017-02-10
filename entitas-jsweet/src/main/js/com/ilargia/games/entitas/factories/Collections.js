/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var factories;
                (function (factories) {
                    var Collections = (function () {
                        function Collections(factory) {
                            Collections._factory = factory;
                        }
                        Collections.createList = function (clazz) {
                            return Collections._factory.createList(clazz);
                        };
                        Collections.createSet = function (clazz) {
                            return Collections._factory.createSet(clazz);
                        };
                        Collections.createMap = function (keyClazz, valueClazz) {
                            return Collections._factory.createMap(keyClazz, valueClazz);
                        };
                        return Collections;
                    }());
                    factories.Collections = Collections;
                    Collections["__class"] = "com.ilargia.games.entitas.factories.Collections";
                })(factories = entitas.factories || (entitas.factories = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
