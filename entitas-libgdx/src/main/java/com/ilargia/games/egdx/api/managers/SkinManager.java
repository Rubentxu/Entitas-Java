package com.ilargia.games.egdx.api.managers;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public interface SkinManager<A> extends Manager {

    public void loadAssets(A assetsManager);

    public Skin createSkin(A assetsManager);

}
