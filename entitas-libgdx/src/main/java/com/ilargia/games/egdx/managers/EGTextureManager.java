package com.ilargia.games.egdx.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.ilargia.games.egdx.interfaces.managers.TextureManager;

public class EGTextureManager implements TextureManager<Class<Texture>,Texture> {

    private AssetManager assetManager;

    public EGTextureManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public <A> void loadAsset(String fileName, Class<Texture> id, A[] args) {
        assetManager.get(fileName, id);
    }

    @Override
    public <V> void unloadAsset(String fileName) {
        assetManager.unload(fileName);
    }

    @Override
    public Texture getAsset(String name, Class<Texture> id) {
        return assetManager.get(name, id);
    }

}
