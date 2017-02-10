/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    export class EntityIsNotDestroyedException extends EntitasException {
        public constructor(message : string) {
            super(message + "\nEntity is not destroyed yet!", "Did you manually call entity.Release(pool) yourself? If so, please don\'t :)");
        }
    }
    EntityIsNotDestroyedException["__class"] = "com.ilargia.games.entitas.exceptions.EntityIsNotDestroyedException";
    EntityIsNotDestroyedException["__interfaces"] = ["java.io.Serializable"];


}

