package com.ilargia.games.egdx.interfaces.managers;


import com.badlogic.gdx.audio.Sound;

public interface SoundManager {

    public void playSound(String name);

    public Sound getSount(String fileName);

}
