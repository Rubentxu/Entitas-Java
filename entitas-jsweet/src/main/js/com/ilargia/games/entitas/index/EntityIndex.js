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
                var index;
                (function (index) {
                    var Entity = com.ilargia.games.entitas.Entity;
                    var Collections = com.ilargia.games.entitas.factories.Collections;
                    var EntityIndex = (function (_super) {
                        __extends(EntityIndex, _super);
                        function EntityIndex(group, key) {
                            _super.call(this, group, key);
                            this._index = Collections.createMap(Object, Entity);
                            this.activate();
                        }
                        EntityIndex.prototype.activate = function () {
                            _super.prototype.activate.call(this);
                            this.indexEntities(this._group);
                        };
                        EntityIndex.prototype.getEntities = function (key) {
                            var entities = null;
                            if (!this._index.containsKey(key)) {
                                entities = Collections.createSet(Entity);
                                this._index.put(key, entities);
                                return entities;
                            }
                            return this._index.get(key);
                        };
                        EntityIndex.prototype.clear = function () {
                            for (var index148 = this._index.values().iterator(); index148.hasNext();) {
                                var entities = index148.next();
                                {
                                    for (var index149 = entities.iterator(); index149.hasNext();) {
                                        var entity = index149.next();
                                        {
                                            entity.release(this);
                                        }
                                    }
                                }
                            }
                            this._index.clear();
                        };
                        EntityIndex.prototype.addEntity = function (entity, component) {
                            if (this.getEntities(this._key.getKey(entity, component)).add(entity))
                                entity.retain(this);
                        };
                        EntityIndex.prototype.removeEntity = function (entity, component) {
                            if (this.getEntities(this._key.getKey(entity, component)).remove(entity))
                                entity.release(this);
                        };
                        EntityIndex.prototype.equals = function (o) {
                            if (this === o)
                                return true;
                            if (o == null || this.constructor !== o.constructor)
                                return false;
                            var that = o;
                            if (this.id != null ? !this.id.equals(that.id) : that.id != null)
                                return false;
                            return this._key != null ? this._key.equals(that._key) : that._key == null;
                        };
                        EntityIndex.prototype.hashCode = function () {
                            var result = this.id != null ? this.id.hashCode() : 0;
                            result = 31 * result + (this._key != null ? this._key.hashCode() : 0);
                            return result;
                        };
                        return EntityIndex;
                    }(AbstractEntityIndex));
                    index.EntityIndex = EntityIndex;
                    EntityIndex["__class"] = "com.ilargia.games.entitas.index.EntityIndex";
                    EntityIndex["__interfaces"] = ["com.ilargia.games.entitas.api.IEntityIndex"];
                })(index = entitas.index || (entitas.index = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
