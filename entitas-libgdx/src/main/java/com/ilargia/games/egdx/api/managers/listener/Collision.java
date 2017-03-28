package com.ilargia.games.egdx.api.managers.listener;

@FunctionalInterface
public interface Collision<Collider> {
    void processCollision(Collider colliderA, Collider colliderB, boolean collisionSignal);

}
