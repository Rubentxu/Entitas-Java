/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    export class EntityDoesNotHaveComponentException extends Error {
        public constructor(message : string, index : number) {
            super(message + "\nEntity does not have a component at index " + index); this.message=message + "\nEntity does not have a component at index " + index;
        }
    }
    EntityDoesNotHaveComponentException["__class"] = "com.ilargia.games.entitas.exceptions.EntityDoesNotHaveComponentException";
    EntityDoesNotHaveComponentException["__interfaces"] = ["java.io.Serializable"];


}

