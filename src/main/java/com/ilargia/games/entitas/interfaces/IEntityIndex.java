package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.exceptions.EntityIndexException;

public interface IEntityIndex {
    public void activate() throws EntityIndexException;
    public void deactivate();
}
