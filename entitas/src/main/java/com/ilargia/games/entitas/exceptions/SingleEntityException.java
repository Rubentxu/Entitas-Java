package com.ilargia.games.entitas.exceptions;

public class SingleEntityException extends EntitasException {

    public SingleEntityException(int count) {
        super("Expected exactly one entity in collection but found " + count + "!",
                "Use collection.SingleEntity() only when you are sure that there " +
                        "is exactly one entity.");
    }

}