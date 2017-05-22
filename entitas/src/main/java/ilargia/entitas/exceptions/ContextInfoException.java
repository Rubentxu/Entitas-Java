package ilargia.entitas.exceptions;

import ilargia.entitas.Context;
import ilargia.entitas.api.ContextInfo;
import ilargia.entitas.api.entitas.EntitasException;

public class ContextInfoException extends EntitasException {

    public ContextInfoException(Context pool, ContextInfo contextInfo) {
        super("Invalid ContextInfo for '" + pool + "'!\nExpected " +
                        pool._totalComponents + " componentName(s) but got " +
                        contextInfo.componentNames.length + ":",
                "");
    }
}
