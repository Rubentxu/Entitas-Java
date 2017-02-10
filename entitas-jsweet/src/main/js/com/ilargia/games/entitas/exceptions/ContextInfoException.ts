/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import Context = com.ilargia.games.entitas.Context;

    import ContextInfo = com.ilargia.games.entitas.api.ContextInfo;

    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    export class ContextInfoException extends EntitasException {
        public constructor(pool : Context<any>, contextInfo : ContextInfo) {
            super("Invalid ContextInfo for \'" + pool + "\'!\nExpected " + pool._totalComponents + " componentName(s) but got " + contextInfo.componentNames.length + ":", "");
        }
    }
    ContextInfoException["__class"] = "com.ilargia.games.entitas.exceptions.ContextInfoException";
    ContextInfoException["__interfaces"] = ["java.io.Serializable"];


}

