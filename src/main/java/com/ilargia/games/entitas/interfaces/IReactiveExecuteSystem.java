package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;

import java.util.ArrayList;

public interface IReactiveExecuteSystem extends ISystem {
    void execute(ArrayList<Entity> entities);
}