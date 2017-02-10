/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api {
    import Context = com.ilargia.games.entitas.Context;

    import IMatcher = com.ilargia.games.entitas.api.matcher.IMatcher;

    import Collector = com.ilargia.games.entitas.collector.Collector;

    import GroupEvent = com.ilargia.games.entitas.events.GroupEvent;

    import Stack = java.util.Stack;

    export interface IContext<TEntity extends IEntity> {
        createEntity() : TEntity;

        destroyEntity(entity? : any) : any;

        hasEntity(entity? : any) : any;

        getEntities(matcher? : any) : any;

        getGroup(matcher : IMatcher<TEntity>) : IGroup<TEntity>;

        getTotalComponents() : number;

        getComponentPools() : Stack<IComponent>[];

        getContextInfo() : ContextInfo;

        getCount() : number;

        getReusableEntitiesCount() : number;

        getRetainedEntitiesCount() : number;

        destroyAllEntities();

        clearGroups();

        addEntityIndex(name : string, entityIndex : IEntityIndex);

        getEntityIndex(name : string) : IEntityIndex;

        deactivateAndRemoveEntityIndices();

        resetCreationIndex();

        clearComponentPool(index : number);

        clearComponentPools();

        reset();

        createCollector(matcher? : any, groupEvent? : any) : any;

        createEntityCollector(contexts? : any, matcher? : any, eventType? : any) : any;
    }
}

