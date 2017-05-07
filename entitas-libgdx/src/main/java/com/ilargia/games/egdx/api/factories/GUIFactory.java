package com.ilargia.games.egdx.api.factories;

import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.managers.GUIManager;

public interface GUIFactory<T,M extends GUIManager> {

    void loadAssets(Engine engine);

    T create(M guiManager);
}