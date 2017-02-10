/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api.events {
    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import IEntity = com.ilargia.games.entitas.api.IEntity;

    import IGroup = com.ilargia.games.entitas.api.IGroup;

    export interface GroupUpdated<TEntity extends IEntity> {
        (group : IGroup<TEntity>, entity : TEntity, index : number, previousComponent : IComponent, newComponent : IComponent);
    }
}

