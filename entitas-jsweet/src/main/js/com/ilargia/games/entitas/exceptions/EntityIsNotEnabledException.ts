/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    export class EntityIsNotEnabledException extends Error {
        public constructor(message : string) {
            super(message + "\nEntity is not enabled!"); this.message=message + "\nEntity is not enabled!";
        }
    }
    EntityIsNotEnabledException["__class"] = "com.ilargia.games.entitas.exceptions.EntityIsNotEnabledException";
    EntityIsNotEnabledException["__interfaces"] = ["java.io.Serializable"];


}

