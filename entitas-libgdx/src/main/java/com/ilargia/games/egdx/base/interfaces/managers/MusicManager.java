package com.ilargia.games.egdx.base.interfaces.managers;


public interface MusicManager<Music> {

    public void playMusic(String name);

    public void stopMusic();

    public Music getMusic(String fileName);


}
