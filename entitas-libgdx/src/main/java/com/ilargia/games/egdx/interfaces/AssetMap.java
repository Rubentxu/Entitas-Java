package com.ilargia.games.egdx.interfaces;


public interface AssetMap<V,K> {

    public void dispose (V value);

    public <Args> boolean  loadAsset(String fileName, K id,Args... args);

    public V getAsset(K id);

    public void clearAssets();

}
