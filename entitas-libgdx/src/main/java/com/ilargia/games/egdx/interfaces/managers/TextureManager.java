package com.ilargia.games.egdx.interfaces.managers;


public interface TextureManager<K,V> extends AssetManager<K,V> {

    public V getTexture(String name);


}
