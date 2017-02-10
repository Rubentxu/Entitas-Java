/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api {
    import Stack = java.util.Stack;

    export interface FactoryEntity<E extends IEntity> {
        create(totalComponents : number, componentPools : Stack<IComponent>[], contextInfo : ContextInfo) : E;
    }
}

