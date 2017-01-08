package com.ilargia.games.egdx.base.interfaces.managers;


public interface SoundManager<Sound> extends Manager{

    public void loadSound(String name);

    public void unloadSound(String name);

    public void playSound(String name);

    public Sound getSount(String fileName);

}
