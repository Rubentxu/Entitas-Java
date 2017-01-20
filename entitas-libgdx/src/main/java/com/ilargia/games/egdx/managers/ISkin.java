package com.ilargia.games.egdx.managers;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public interface ISkin {

    public void loadAssets(EGAssetsManager assetsManager);

    public Skin createSkin(EGAssetsManager assetsManager);

}
