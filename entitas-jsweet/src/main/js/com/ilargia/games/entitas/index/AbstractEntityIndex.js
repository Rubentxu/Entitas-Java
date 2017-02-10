/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var index;
                (function (index_1) {
                    var UUID = java.util.UUID;
                    var AbstractEntityIndex = (function () {
                        function AbstractEntityIndex(group, key) {
                            var _this = this;
                            this.id = UUID.randomUUID();
                            this.onEntityAdded = function (group, entity, index, component) {
                                _this.addEntity(entity, component);
                            };
                            this.onEntityRemoved = function (group, entity, index, component) {
                                _this.removeEntity(entity, component);
                            };
                            this._group = group;
                            this._key = key;
                        }
                        AbstractEntityIndex.prototype.activate = function () {
                            this._group.OnEntityAdded(this.onEntityAdded);
                            this._group.OnEntityRemoved(this.onEntityRemoved);
                        };
                        AbstractEntityIndex.prototype.deactivate = function () {
                            this._group.__OnEntityAdded.remove(this.onEntityAdded);
                            this._group.__OnEntityRemoved.remove(this.onEntityRemoved);
                            this.clear();
                        };
                        AbstractEntityIndex.prototype.indexEntities = function (group) {
                            var entities = group.getEntities();
                            for (var i = 0; i < entities.length; i++) {
                                this.addEntity(entities[i], null);
                            }
                        };
                        return AbstractEntityIndex;
                    }());
                    index_1.AbstractEntityIndex = AbstractEntityIndex;
                    AbstractEntityIndex["__class"] = "com.ilargia.games.entitas.index.AbstractEntityIndex";
                    AbstractEntityIndex["__interfaces"] = ["com.ilargia.games.entitas.api.IEntityIndex"];
                })(index = entitas.index || (entitas.index = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
