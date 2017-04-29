package com.ilargia.games.egdx.api.factories;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.managers.GUIManager;
import com.ilargia.games.entitas.api.IEntity;

public interface GUIFactory<T,M extends GUIManager> {

    void loadAssets(Engine engine);

    T create(M guiManager);
}