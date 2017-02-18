package com.ilargia.games.egdx.api.managers;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ilargia.games.egdx.api.GUIFactory;
import com.ilargia.games.entitas.api.IEntity;

public interface GUIManager<A> extends Manager {

    void initialize();

    void loadAssets(A assetsManager);

    Skin createSkin(A assetsManager);

    void addGUIFactory(String name, GUIFactory factory);

    <TEntity extends IEntity> TEntity createEntity(String name, float posX, float posY);

}
