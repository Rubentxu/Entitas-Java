/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import Context = com.ilargia.games.entitas.Context;

    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    export class ContextEntityIndexDoesAlreadyExistException extends EntitasException {
        public constructor(pool : Context<any>, name : string) {
            super("Cannot add EntityIndex \'" + name + "\' to pool \'" + pool + "\'!", "An EntityIndex with this name has already been added.");
        }
    }
    ContextEntityIndexDoesAlreadyExistException["__class"] = "com.ilargia.games.entitas.exceptions.ContextEntityIndexDoesAlreadyExistException";
    ContextEntityIndexDoesAlreadyExistException["__interfaces"] = ["java.io.Serializable"];


}

