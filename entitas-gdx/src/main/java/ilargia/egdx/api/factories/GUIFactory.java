package ilargia.egdx.api.factories;

import ilargia.egdx.api.Engine;
import ilargia.egdx.api.managers.GUIManager;

public interface GUIFactory<T,M extends GUIManager> {

    void loadAssets(Engine engine);

    T create(M guiManager);
}