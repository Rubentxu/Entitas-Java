/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.index {
    import Entity = com.ilargia.games.entitas.Entity;

    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import IEntity = com.ilargia.games.entitas.api.IEntity;

    import IGroup = com.ilargia.games.entitas.api.IGroup;

    import EntityIndexException = com.ilargia.games.entitas.exceptions.EntityIndexException;

    import Collections = com.ilargia.games.entitas.factories.Collections;

    import Map = java.util.Map;

    import Set = java.util.Set;

    export class EntityIndex<TEntity extends Entity, TKey> extends AbstractEntityIndex<TEntity, TKey> {
        private _index : Map<TKey, Set<TEntity>>;

        public constructor(group : IGroup<TEntity>, key : AbstractEntityIndex.Func<TEntity, IComponent, TKey>) {
            super(group, key);
            this._index = Collections.createMap(Object, Entity);
            this.activate();
        }

        public activate() {
            super.activate();
            this.indexEntities(this._group);
        }

        public getEntities(key : TKey) : Set<TEntity> {
            let entities : Set<TEntity> = null;
            if(!this._index.containsKey(key)) {
                entities = Collections.createSet<any>(Entity);
                this._index.put(key, entities);
                return entities;
            }
            return this._index.get(key);
        }

        clear() {
            for(let index148=this._index.values().iterator();index148.hasNext();) {
                let entities = index148.next();
                {
                    for(let index149=entities.iterator();index149.hasNext();) {
                        let entity = index149.next();
                        {
                            entity.release(this);
                        }
                    }
                }
            }
            this._index.clear();
        }

        addEntity(entity : TEntity, component : IComponent) {
            if(this.getEntities(this._key.getKey(entity, component)).add(entity)) entity.retain(this);
        }

        removeEntity(entity : TEntity, component : IComponent) {
            if(this.getEntities(this._key.getKey(entity, component)).remove(entity)) entity.release(this);
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
    EntityIndex["__class"] = "com.ilargia.games.entitas.index.EntityIndex";
    EntityIndex["__interfaces"] = ["com.ilargia.games.entitas.api.IEntityIndex"];


}

