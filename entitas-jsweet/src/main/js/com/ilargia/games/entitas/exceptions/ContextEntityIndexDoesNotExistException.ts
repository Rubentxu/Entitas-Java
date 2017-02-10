/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import Context = com.ilargia.games.entitas.Context;

    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    export class ContextEntityIndexDoesNotExistException extends EntitasException {
        public constructor(pool : Context<any>, name : string) {
            super("Cannot get EntityIndex \'" + name + "\' from pool \'" + pool + "\'!", "No EntityIndex with this name has been added.");
        }
    }
    ContextEntityIndexDoesNotExistException["__class"] = "com.ilargia.games.entitas.exceptions.ContextEntityIndexDoesNotExistException";
    ContextEntityIndexDoesNotExistException["__interfaces"] = ["java.io.Serializable"];


}

