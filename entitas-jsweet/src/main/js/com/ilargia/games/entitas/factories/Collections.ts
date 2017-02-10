/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.factories {
    import List = java.util.List;

    import Map = java.util.Map;

    import Set = java.util.Set;

    export class Collections {
        static _factory : CollectionsFactory;

        public constructor(factory : CollectionsFactory) {
            Collections._factory = factory;
        }

        public static createList(clazz : any) : List<any> {
            return Collections._factory.createList<any>(clazz);
        }

        public static createSet<T>(clazz : any) : Set<any> {
            return Collections._factory.createSet<any>(clazz);
        }

        public static createMap(keyClazz : any, valueClazz : any) : Map<any, any> {
            return Collections._factory.createMap<any, any>(keyClazz, valueClazz);
        }
    }
    Collections["__class"] = "com.ilargia.games.entitas.factories.Collections";

}

