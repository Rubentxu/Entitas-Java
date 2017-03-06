package com.ilargia.games.egdx.api.managers;


public interface PhysicsManager<P, L> extends Manager {

    void addListener(L listener);

    P getPhysics();

    <B> B getBodyBuilder();
}
