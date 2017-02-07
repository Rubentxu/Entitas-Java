package com.ilargia.games.egdx.managers;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ilargia.games.egdx.base.interfaces.managers.Manager;

public interface ISkin extends Manager {

    public void loadAssets(EGAssetsManager assetsManager);

    public Skin createSkin(EGAssetsManager assetsManager);

}
