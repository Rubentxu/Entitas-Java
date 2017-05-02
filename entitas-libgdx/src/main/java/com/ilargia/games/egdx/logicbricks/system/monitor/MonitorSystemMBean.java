package com.ilargia.games.egdx.logicbricks.system.monitor;


import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;

public interface MonitorSystemMBean {

    public Entitas getEntitas();

    public EngineGDX getEngine();

}
