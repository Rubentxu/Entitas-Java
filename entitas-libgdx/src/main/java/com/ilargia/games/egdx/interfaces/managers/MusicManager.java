package com.ilargia.games.egdx.interfaces.managers;


import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.audio.Music;

public interface MusicManager {

    public void playMusic(String name);

    public void stopMusic();

    public Music getMusic(String fileName);


}
