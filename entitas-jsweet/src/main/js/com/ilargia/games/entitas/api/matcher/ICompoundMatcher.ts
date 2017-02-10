/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api.matcher {
    import IEntity = com.ilargia.games.entitas.api.IEntity;

    export interface ICompoundMatcher<TEntity extends IEntity> extends IMatcher<TEntity> {
        getAllOfIndices() : number[];

        getAnyOfIndices() : number[];

        getNoneOfIndices() : number[];
    }
}

