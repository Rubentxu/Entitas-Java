package com.ilargia.games.entitas.api;


import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.group.GroupEvent;

import java.util.Stack;

public interface IContexts {
    public IContext[] allContexts();
}
