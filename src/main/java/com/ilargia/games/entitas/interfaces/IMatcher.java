package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;

import java.util.List;

public interface IMatcher {

    List<Integer> getindices();

    boolean matches(Entity entity);

}