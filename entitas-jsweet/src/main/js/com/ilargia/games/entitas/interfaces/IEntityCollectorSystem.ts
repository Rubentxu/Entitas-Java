/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.interfaces {
    import Collector = com.ilargia.games.entitas.collector.Collector;

    export interface IEntityCollectorSystem extends IReactiveExecuteSystem<any> {
        getEntityCollector() : Collector<any>;
    }
}

