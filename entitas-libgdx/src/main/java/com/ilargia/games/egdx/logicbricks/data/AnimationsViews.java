package com.ilargia.games.egdx.logicbricks.data;


import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class AnimationsViews {
    public Map<String, AnimationsView> views;

    public AnimationsViews() {
        this.views = EntitasCollections.createMap(String.class, AnimationsView.class);

    }

}
