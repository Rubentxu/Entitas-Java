package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.api.entitas.EntitasException;

public class ContextInfoException extends EntitasException {

    public ContextInfoException(Context pool, ContextInfo contextInfo) {
        super("Invalid ContextInfo for '" + pool + "'!\nExpected " +
                        pool._totalComponents + " componentName(s) but got " +
                        contextInfo.componentNames.length + ":",
                "");
    }
}
