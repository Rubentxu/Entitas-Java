/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.factories {
    import List = java.util.List;

    import Map = java.util.Map;

    import Set = java.util.Set;

    export interface CollectionsFactory {
        createList<T>(clazz : any) : List<any>;

        createSet<T>(clazz : any) : Set<any>;

        createMap<K, V>(keyClazz : any, valueClazz : any) : Map<any, any>;
    }
}

