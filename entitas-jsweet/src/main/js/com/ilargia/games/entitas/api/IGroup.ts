/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api {
    import GroupChanged = com.ilargia.games.entitas.api.events.GroupChanged;

    import IMatcher = com.ilargia.games.entitas.api.matcher.IMatcher;

    import Set = java.util.Set;

    export interface IGroup<TEntity extends IEntity> {
        getCount() : number;

        removeAllEventHandlers();

        getMatcher() : IMatcher<TEntity>;

        handleEntitySilently(entity : TEntity);

        handleEntity(entity? : any, index? : any, component? : any) : any;

        updateEntity(entity : TEntity, index : number, previousComponent : IComponent, newComponent : IComponent);

        containsEntity(entity : TEntity) : boolean;

        getEntities() : TEntity[];

        getSingleEntity() : TEntity;
    }
}

