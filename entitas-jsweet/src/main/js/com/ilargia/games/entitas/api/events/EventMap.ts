/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api.events {
    import Collections = com.ilargia.games.entitas.factories.Collections;

    import Collection = java.util.Collection;

    import Map = java.util.Map;

    export class EventMap<K, V> {
        private __listeners : Map<K, V>;

        public constructor() {
            this.__listeners = Collections.createMap(Object, Object);
        }

        public removeListener(key : K) : V {
            return this.__listeners.remove(key);
        }

        public addListener(key : K, eventHandler : V) : V {
            this.__listeners.put(key, eventHandler);
            return eventHandler;
        }

        public listeners() : Collection<V> {
            return this.__listeners.values();
        }

        public getEventHandler(key : K) : V {
            return this.__listeners.get(key);
        }

        public clear() {
            this.__listeners.clear();
        }
    }
    EventMap["__class"] = "com.ilargia.games.entitas.api.events.EventMap";

}

