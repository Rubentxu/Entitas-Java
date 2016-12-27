package com.ilargia.games.egdx.interfaces.managers;


public interface SoundManager<Sound> {

    public void playSound(String name);

    public Sound getSount(String fileName);

}
