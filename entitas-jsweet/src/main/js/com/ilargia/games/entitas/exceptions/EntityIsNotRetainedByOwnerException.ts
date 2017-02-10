/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    export class EntityIsNotRetainedByOwnerException extends Error {
        public constructor(owner : any) {
            super("Entity is not retained by owner: " + owner); this.message="Entity is not retained by owner: " + owner;
        }
    }
    EntityIsNotRetainedByOwnerException["__class"] = "com.ilargia.games.entitas.exceptions.EntityIsNotRetainedByOwnerException";
    EntityIsNotRetainedByOwnerException["__interfaces"] = ["java.io.Serializable"];


}

