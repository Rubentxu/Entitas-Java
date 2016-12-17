package com.ilargia.games.egdx.interfaces.managers;


public interface FontManager<K,V> extends AssetManager<K,V> {

    public V getFont(String name);


}
