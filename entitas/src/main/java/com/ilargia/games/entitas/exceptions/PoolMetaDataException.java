package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.BasePool;
import com.ilargia.games.entitas.EntityMetaData;

public class PoolMetaDataException extends EntitasException {

    public PoolMetaDataException(BasePool pool, EntityMetaData entityMetaData) {
        super("Invalid EntityMetaData for '" + pool + "'!\nExpected " +
                        pool._totalComponents + " componentName(s) but got " +
                        entityMetaData.componentNames.length + ":",
                "");
    }
}
