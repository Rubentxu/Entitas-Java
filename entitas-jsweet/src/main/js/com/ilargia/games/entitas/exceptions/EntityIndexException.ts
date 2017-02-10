/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    export class EntityIndexException extends EntitasException {
        public constructor(message : string, hint : string) {
            super(message, hint);
        }
    }
    EntityIndexException["__class"] = "com.ilargia.games.entitas.exceptions.EntityIndexException";
    EntityIndexException["__interfaces"] = ["java.io.Serializable"];


}

