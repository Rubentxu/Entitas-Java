package com.ilargia.games.entitas.api;

import com.ilargia.games.entitas.Context;

@FunctionalInterface
public interface IEntitas {
    Context[] allContexts();
}
