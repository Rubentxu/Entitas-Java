/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    export class EntityAlreadyHasComponentException extends EntitasException {
        public constructor(index : number, message : string, hint : string) {
            super(message + "\nEntity already has a component at index " + index + "!", hint);
        }
    }
    EntityAlreadyHasComponentException["__class"] = "com.ilargia.games.entitas.exceptions.EntityAlreadyHasComponentException";
    EntityAlreadyHasComponentException["__interfaces"] = ["java.io.Serializable"];


}

