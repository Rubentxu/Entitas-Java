/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import Context = com.ilargia.games.entitas.Context;

    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    export class ContextStillHasRetainedEntitiesException extends EntitasException {
        public constructor(pool : Context<any>) {
            super("\'" + pool + "\' detected retained entities although all entities got destroyed!", "Did you release all entities? Try calling pool.ClearGroups() and system.ClearReactiveSystems() before calling pool.DestroyAllEntities() to avoid memory leaks.");
        }
    }
    ContextStillHasRetainedEntitiesException["__class"] = "com.ilargia.games.entitas.exceptions.ContextStillHasRetainedEntitiesException";
    ContextStillHasRetainedEntitiesException["__interfaces"] = ["java.io.Serializable"];


}

