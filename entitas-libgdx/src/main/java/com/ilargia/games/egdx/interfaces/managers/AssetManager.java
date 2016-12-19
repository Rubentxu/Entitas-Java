package com.ilargia.games.egdx.interfaces.managers;

public interface AssetManager<K, V> extends Manager {

    public <A> void loadAsset(String fileName, K id, A... args);

    public <V> void unloadAsset(String fileName);

    public V getAsset(String name, K id);

}
