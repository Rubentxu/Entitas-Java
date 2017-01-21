package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.BaseContext;
import com.ilargia.games.entitas.ContextInfo;

public class ContextInfoException extends EntitasException {

    public ContextInfoException(BaseContext pool, ContextInfo contextInfo) {
        super("Invalid ContextInfo for '" + pool + "'!\nExpected " +
                        pool._totalComponents + " componentName(s) but got " +
                        contextInfo.componentNames.length + ":",
                "");
    }
}
