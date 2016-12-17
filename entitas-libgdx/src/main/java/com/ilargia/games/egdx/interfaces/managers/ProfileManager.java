package com.ilargia.games.egdx.interfaces.managers;


public interface ProfileManager<P> {

    public P retrieveProfile();

    public void persist(P profile);

    public void resetToDefaultProfile();

    public P getProfile();

}
