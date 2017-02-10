/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api.matcher {
    import IEntity = com.ilargia.games.entitas.api.IEntity;

    export interface IAnyOfMatcher<TEntity extends IEntity> extends INoneOfMatcher<TEntity> {
        noneOf(...indices : any[]) : any;
    }
}

