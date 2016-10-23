package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.Pool;
import com.ilargia.games.entitas.PoolMetaData;

public class PoolMetaDataException extends EntitasException {

    public PoolMetaDataException(Pool pool, PoolMetaData poolMetaData) {
        super("Invalid PoolMetaData for '" + pool + "'!\nExpected " +
                        pool._totalComponents + " componentName(s) but got " +
                        poolMetaData.componentNames.length + ":",
                "");
    }
}
