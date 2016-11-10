package com.ilargia.games.entitas.extensions;

import com.ilargia.games.entitas.Entity;

import java.util.Collection;

public final class CollectionExtension {

    public static Entity singleEntity(Collection<Entity> collection) {
        if (collection.size() != 1) {
            throw new RuntimeException("Expected exactly one entity but found " + collection.size());
        }

        return collection.iterator().next();
    }
}