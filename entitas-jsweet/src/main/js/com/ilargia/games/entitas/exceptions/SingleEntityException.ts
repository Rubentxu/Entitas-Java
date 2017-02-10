/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    export class SingleEntityException extends EntitasException {
        public constructor(count : number) {
            super("Expected exactly one entity in collection but found " + count + "!", "Use collection.SingleEntity() only when you are sure that there is exactly one entity.");
        }
    }
    SingleEntityException["__class"] = "com.ilargia.games.entitas.exceptions.SingleEntityException";
    SingleEntityException["__interfaces"] = ["java.io.Serializable"];


}

