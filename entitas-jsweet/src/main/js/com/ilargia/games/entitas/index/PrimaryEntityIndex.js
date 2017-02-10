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
                    var EntityIndexException = com.ilargia.games.entitas.exceptions.EntityIndexException;
                    var Collections = com.ilargia.games.entitas.factories.Collections;
                    var PrimaryEntityIndex = (function (_super) {
                        __extends(PrimaryEntityIndex, _super);
                        function PrimaryEntityIndex(group, getKey) {
                            _super.call(this, group, getKey);
                            this._index = Collections.createMap(Object, Object);
                            this.activate();
                        }
                        PrimaryEntityIndex.prototype.activate = function () {
                            _super.prototype.activate.call(this);
                            this.indexEntities(this._group);
                        };
                        PrimaryEntityIndex.prototype.hasEntity = function (key) {
                            return this._index.containsKey(key);
                        };
                        PrimaryEntityIndex.prototype.getEntity = function (key) {
                            var entity = this.tryGetEntity(key);
                            if (entity == null) {
                                throw new EntityIndexException("Entity for key \'" + key + "\' doesn\'t exist!", "You should check if an entity with that key exists before getting it.");
                            }
                            return entity;
                        };
                        PrimaryEntityIndex.prototype.tryGetEntity = function (key) {
                            var entity = null;
                            this._index.get(key);
                            return entity;
                        };
                        PrimaryEntityIndex.prototype.addEntity = function (entity, component) {
                            var key = this._key.getKey(entity, component);
                            if (this._index.containsKey(key)) {
                                throw new EntityIndexException("Entity for key \'" + key + "\' already exists!", "Only one entity for a primary key is allowed.");
                            }
                            this._index.put(key, entity);
                            entity.retain(this);
                        };
                        PrimaryEntityIndex.prototype.removeEntity = function (entity, component) {
                            this._index.remove(this._key.getKey(entity, component));
                            entity.release(this);
                        };
                        PrimaryEntityIndex.prototype.clear = function () {
                            for (var index150 = this._index.values().iterator(); index150.hasNext();) {
                                var entity = index150.next();
                                {
                                    entity.release(this);
                                }
                            }
                            this._index.clear();
                        };
                        PrimaryEntityIndex.prototype.equals = function (o) {
                            if (this === o)
                                return true;
                            if (o == null || this.constructor !== o.constructor)
                                return false;
                            var that = o;
                            if (this.id != null ? !this.id.equals(that.id) : that.id != null)
                                return false;
                            return this._key != null ? this._key.equals(that._key) : that._key == null;
                        };
                        PrimaryEntityIndex.prototype.hashCode = function () {
                            var result = this.id != null ? this.id.hashCode() : 0;
                            result = 31 * result + (this._key != null ? this._key.hashCode() : 0);
                            return result;
                        };
                        return PrimaryEntityIndex;
                    }(AbstractEntityIndex));
                    index.PrimaryEntityIndex = PrimaryEntityIndex;
                    PrimaryEntityIndex["__class"] = "com.ilargia.games.entitas.index.PrimaryEntityIndex";
                    PrimaryEntityIndex["__interfaces"] = ["com.ilargia.games.entitas.api.IEntityIndex"];
                })(index = entitas.index || (entitas.index = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
