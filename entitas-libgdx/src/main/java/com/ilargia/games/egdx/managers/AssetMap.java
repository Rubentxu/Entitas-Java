package com.ilargia.games.egdx.managers;


import com.badlogic.gdx.utils.ObjectMap;

public abstract class AssetMap<K,V> {
    private ObjectMap<K,V> _map;

    public AssetMap() {
        _map = new ObjectMap<K, V>();
    }

    public abstract <A> boolean loadAsset(String fileName, K id, A ...args);

    public V getAsset(K id) {
        return _map.get(id);
    }

    public void clearAssets() {
        _map.clear();
    }

}
