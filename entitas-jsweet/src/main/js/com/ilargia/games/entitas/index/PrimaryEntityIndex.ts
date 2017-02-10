/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.index {
    import Entity = com.ilargia.games.entitas.Entity;

    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import IGroup = com.ilargia.games.entitas.api.IGroup;

    import EntityIndexException = com.ilargia.games.entitas.exceptions.EntityIndexException;

    import Collections = com.ilargia.games.entitas.factories.Collections;

    import Map = java.util.Map;

    export class PrimaryEntityIndex<TEntity extends Entity, TKey> extends AbstractEntityIndex<TEntity, TKey> {
        private _index : Map<TKey, TEntity>;

        public constructor(group : IGroup<any>, getKey : AbstractEntityIndex.Func<TEntity, IComponent, TKey>) {
            super(group, getKey);
            this._index = Collections.createMap(Object, Object);
            this.activate();
        }

        public activate() {
            super.activate();
            this.indexEntities(this._group);
        }

        public hasEntity(key : TKey) : boolean {
            return this._index.containsKey(key);
        }

        public getEntity(key : TKey) : TEntity {
            let entity : TEntity = this.tryGetEntity(key);
            if(entity == null) {
                throw new EntityIndexException("Entity for key \'" + key + "\' doesn\'t exist!", "You should check if an entity with that key exists before getting it.");
            }
            return entity;
        }

        public tryGetEntity(key : TKey) : TEntity {
            let entity : TEntity = null;
            this._index.get(key);
            return entity;
        }

        addEntity(entity : TEntity, component : IComponent) {
            let key : TKey = this._key.getKey(entity, component);
            if(this._index.containsKey(key)) {
                throw new EntityIndexException("Entity for key \'" + key + "\' already exists!", "Only one entity for a primary key is allowed.");
            }
            this._index.put(key, entity);
            entity.retain(this);
        }

        removeEntity(entity : TEntity, component : IComponent) {
            this._index.remove(this._key.getKey(entity, component));
            entity.release(this);
        }

        clear() {
            for(let index150=this._index.values().iterator();index150.hasNext();) {
                let entity = index150.next();
                {
                    entity.release(this);
                }
            }
            this._index.clear();
        }

        public equals(o : any) : boolean {
            if(this === o) return true;
            if(o == null || (<any>this.constructor) !== (<any>o.constructor)) return false;
            let that : AbstractEntityIndex<any, any> = <AbstractEntityIndex<any, any>>o;
            if(this.id != null?!this.id.equals(that.id):that.id != null) return false;
            return this._key != null?this._key.equals(that._key):that._key == null;
        }

        public hashCode() : number {
            let result : number = this.id != null?this.id.hashCode():0;
            result = 31 * result + (this._key != null?this._key.hashCode():0);
            return result;
        }
    }
    PrimaryEntityIndex["__class"] = "com.ilargia.games.entitas.index.PrimaryEntityIndex";
    PrimaryEntityIndex["__interfaces"] = ["com.ilargia.games.entitas.api.IEntityIndex"];


}

