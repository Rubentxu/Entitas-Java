/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    export class EntityIsAlreadyRetainedByOwnerException extends Error {
        public constructor(owner : any) {
            super("Entity is already retained by owner: " + owner); this.message="Entity is already retained by owner: " + owner;
        }
    }
    EntityIsAlreadyRetainedByOwnerException["__class"] = "com.ilargia.games.entitas.exceptions.EntityIsAlreadyRetainedByOwnerException";
    EntityIsAlreadyRetainedByOwnerException["__interfaces"] = ["java.io.Serializable"];


}

