/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.interfaces {
    import TriggerOnEvent = com.ilargia.games.entitas.matcher.TriggerOnEvent;

    export interface IMultiReactiveSystem extends IReactiveExecuteSystem<any> {
        getTriggers() : TriggerOnEvent[];
    }
}

