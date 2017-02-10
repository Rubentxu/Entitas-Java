/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.interfaces {
    import Entity = com.ilargia.games.entitas.Entity;

    import ISystem = com.ilargia.games.entitas.api.system.ISystem;

    import List = java.util.List;

    export interface IReactiveExecuteSystem<E extends Entity> extends ISystem {
        execute(entities : List<Entity>);
    }
}

