/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    export class ContextDoesNotContainEntityException extends EntitasException {
        public constructor(message : string, hint : string) {
            super(message + "\nSplashPool does not contain entity!", hint);
        }
    }
    ContextDoesNotContainEntityException["__class"] = "com.ilargia.games.entitas.exceptions.ContextDoesNotContainEntityException";
    ContextDoesNotContainEntityException["__interfaces"] = ["java.io.Serializable"];


}

