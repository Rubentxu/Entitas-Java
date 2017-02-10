/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api.events {
    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import IEntity = com.ilargia.games.entitas.api.IEntity;

    export interface EntityComponentReplaced<TEntity extends IEntity> {
        (entity : TEntity, index : number, previousComponent : IComponent, newComponent : IComponent);
    }
}

