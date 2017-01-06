package com.ilargia.games.egdx.base.interfaces.managers;


public interface ProfileManager<P> extends Manager {

    public P retrieveProfile();

    public void persist(P profile);

    public void resetToDefaultProfile();

    public P getProfile();

}
