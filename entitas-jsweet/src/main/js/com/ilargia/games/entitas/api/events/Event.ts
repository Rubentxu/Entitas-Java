/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api.events {
    import Context = com.ilargia.games.entitas.Context;

    import Set = java.util.Set;

    export class Event<T> {
        private __listeners : Set<T>;

        public constructor() {
        }

        public removeListener(eventHandler : T) : boolean {
            return this.__listeners.remove(eventHandler);
        }

        public addListener(eventHandler : T) : T {
            this.__listeners.add(eventHandler);
            return eventHandler;
        }

        public listeners() : Set<T> {
            return this.__listeners;
        }

        public clear<P extends Context<any>>() {
            this.__listeners.clear();
        }
    }
    Event["__class"] = "com.ilargia.games.entitas.api.events.Event";

}

