/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.index {
    import Entity = com.ilargia.games.entitas.Entity;

    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import IEntityIndex = com.ilargia.games.entitas.api.IEntityIndex;

    import IGroup = com.ilargia.games.entitas.api.IGroup;

    import GroupChanged = com.ilargia.games.entitas.api.events.GroupChanged;

    import Group = com.ilargia.games.entitas.group.Group;

    import UUID = java.util.UUID;

    export abstract class AbstractEntityIndex<TEntity extends Entity, TKey> implements IEntityIndex {
        id : UUID = UUID.randomUUID();

        _group : Group<TEntity>;

        _key : AbstractEntityIndex.Func<TEntity, IComponent, TKey>;

        onEntityAdded : GroupChanged<TEntity> = (group, entity, index, component) => {
            this.addEntity(entity, component);
        };

        onEntityRemoved : GroupChanged<TEntity> = (group, entity, index, component) => {
            this.removeEntity(entity, component);
        };

        constructor(group : IGroup<TEntity>, key : AbstractEntityIndex.Func<TEntity, IComponent, TKey>) {
            this._group = <Group<TEntity>>group;
            this._key = key;
        }

        public activate() {
            this._group.OnEntityAdded(this.onEntityAdded);
            this._group.OnEntityRemoved(this.onEntityRemoved);
        }

        public deactivate() {
            this._group.__OnEntityAdded.remove(this.onEntityAdded);
            this._group.__OnEntityRemoved.remove(this.onEntityRemoved);
            this.clear();
        }

        indexEntities(group : IGroup<TEntity>) {
            let entities : TEntity[] = group.getEntities();
            for(let i : number = 0; i < entities.length; i++) {
                this.addEntity(entities[i], null);
            }
        }

        abstract addEntity(entity : TEntity, component : IComponent);

        abstract removeEntity(entity : TEntity, component : IComponent);

        abstract clear();
    }
    AbstractEntityIndex["__class"] = "com.ilargia.games.entitas.index.AbstractEntityIndex";
    AbstractEntityIndex["__interfaces"] = ["com.ilargia.games.entitas.api.IEntityIndex"];



    export namespace AbstractEntityIndex {

        export interface Func<TEntity, IComponent, TKey> {
            getKey(entity : TEntity, component : IComponent) : TKey;
        }
    }

}

