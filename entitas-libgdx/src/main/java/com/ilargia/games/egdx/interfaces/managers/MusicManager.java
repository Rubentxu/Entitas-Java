package com.ilargia.games.egdx.interfaces.managers;


import com.badlogic.gdx.assets.*;

public interface MusicManager<K,V> extends AssetManager<K,V> {

    public void playMusic(String name);

    public void stopMusic();


}
