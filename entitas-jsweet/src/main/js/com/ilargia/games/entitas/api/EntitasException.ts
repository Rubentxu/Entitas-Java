/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api {
    export class EntitasException extends Error {
        public constructor(message : string, hint : string) {
            super();
            new Error(hint != null?(message + "\n" + hint):message);
        }
    }
    EntitasException["__class"] = "com.ilargia.games.entitas.api.EntitasException";
    EntitasException["__interfaces"] = ["java.io.Serializable"];


}

