package com.ilargia.games.egdx.base.interfaces.managers;


public interface MusicManager<Music> extends Manager {

    public void loadMusic(String name);

    public void unloadMusic(String name);

    public void playMusic(String name);

    public void stopMusic();

    public Music getMusic(String fileName);


}
