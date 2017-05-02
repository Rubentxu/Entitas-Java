package com.ilargia.games.egdx.logicbricks.system.monitor;

import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.api.system.IInitializeSystem;

import java.lang.management.*;
import javax.management.*;


public class MonitorSystem implements IInitializeSystem, MonitorSystemMBean {

    private final EngineGDX engine;
    private final Entitas entitas;
    static
    {
        System.loadLibrary("attach");
    }

    public MonitorSystem(Entitas entitas, EngineGDX engine) {
        this.engine = engine;
        this.entitas = entitas;

    }

    @Override
    public void initialize() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName monitorName = new ObjectName("com.ilargia.games.egdx.logicbricks.system.monitor:type=MonitorSystem");

            mbs.registerMBean(this, monitorName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Entitas getEntitas() {
        return entitas;
    }

    @Override
    public EngineGDX getEngine() {
        return engine;
    }
}
