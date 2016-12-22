package com.ilargia.games.egdx.interfaces.managers;

public interface AssetManager<K> extends Manager {

    void loadAsset(String fileName, K id);

    void unloadAsset(String fileName);

//    <V> V getAsset(String name, K id);

}
