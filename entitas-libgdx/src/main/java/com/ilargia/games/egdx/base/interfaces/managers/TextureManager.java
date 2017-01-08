package com.ilargia.games.egdx.base.interfaces.managers;


public interface TextureManager<Texture,Atlas> extends Manager {

    public void loadTexture(String name);

    public void unloadTexture(String name);

    public Texture getTexture(String name);

    public void loadTextureAtlas(String name);

    public void unloadTextureAtlas(String name);

    public Atlas getTextureAtlas(String name);

}
