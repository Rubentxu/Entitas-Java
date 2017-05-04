package com.ilargia.games.egdx.logicbricks.system.monitor;

import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorComponentsLookup;
import com.ilargia.games.egdx.logicbricks.gen.game.GameComponentsLookup;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorComponentsLookup;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.j256.simplejmx.common.JmxAttributeField;
import com.j256.simplejmx.common.JmxAttributeMethod;
import com.j256.simplejmx.common.JmxResource;
import com.j256.simplejmx.server.JmxServer;
import com.j256.simplejmx.web.JmxWebServer;

import javax.management.JMException;


@JmxResource(domainName = "entitas.gdx.monitor", description = "Entitas Gdx Debug JMX Monitor")
public class MonitorSystem implements IInitializeSystem {

    @JmxAttributeField(description = "Engine")
    public final EngineGDX engine;

    @JmxAttributeField(description = "Entitas")
    public final Entitas entitas;

    @JmxAttributeField(description = "Components Game")
    String[] gameComponents = GameComponentsLookup.componentNames();


    @JmxAttributeField(description = "Components Sensor")
    String[] sensorComponents = SensorComponentsLookup.componentNames();

    @JmxAttributeField(description = "Components Actuator")
    String[] actuatorComponents = ActuatorComponentsLookup.componentNames();


    public MonitorSystem(Entitas entitas, EngineGDX engine) {
        this.engine = engine;
        this.entitas = entitas;

    }

    @Override
    public void initialize() {
        JmxServer jmxServer = new JmxServer(8000);

        try {
            jmxServer.start();
            jmxServer.register(this);
            // create a web server listening on a specific port
            JmxWebServer jmxWebServer = new JmxWebServer(8001);
            jmxWebServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // un-register objects
            // stop our server
            jmxServer.stop();
        }
    }

    @JmxAttributeMethod(description = "Total Game Components")
    public int getGameTotalComponents() {
        return entitas.game._totalComponents;
    }

    @JmxAttributeMethod(description = "First game Entity")
    public GameEntity getGameFirstGameEntity() {
        return entitas.game.getEntities()[0];
    }
//
//    @JmxAttributeMethod(description = "Sensor Components")
//    String[] getSensorComponents() {
//        return SensorComponentsLookup.componentNames();
//    }
//
//    @JmxAttributeMethod(description = "Actuator Components")
//    String[] getActuatorComponents() {
//        return ActuatorComponentsLookup.componentNames();
//    }
}
