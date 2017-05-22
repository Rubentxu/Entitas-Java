package ilargia.entitas.exceptions;

import ilargia.entitas.api.entitas.IEntity;

public class EntityIsAlreadyRetainedByOwnerException extends RuntimeException {

    public EntityIsAlreadyRetainedByOwnerException(IEntity entity, Object owner) {
        super("'" + owner + "' cannot release " + entity + "!\n" +
                "Entity is already retained by owner: " + owner +
                "An entity can only be released from objects that retain it.");
    }

}