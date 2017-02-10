/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api.matcher {
    import IEntity = com.ilargia.games.entitas.api.IEntity;

    export interface IAllOfMatcher<TEntity extends IEntity> extends IAnyOfMatcher<TEntity> {
        anyOf(...indices : any[]) : any;
    }
}

