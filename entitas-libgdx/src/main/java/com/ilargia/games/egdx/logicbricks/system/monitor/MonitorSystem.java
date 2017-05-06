package com.ilargia.games.egdx.logicbricks.system.monitor;

import com.google.gson.Gson;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorComponentsLookup;
import com.ilargia.games.egdx.logicbricks.gen.game.GameComponentsLookup;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorComponentsLookup;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.j256.simplejmx.common.JmxAttributeField;
import com.j256.simplejmx.common.JmxAttributeMethod;
import com.j256.simplejmx.common.JmxResource;
import com.j256.simplejmx.server.JmxServer;
import com.j256.simplejmx.web.JmxWebServer;

import java.util.Map;
import java.util.TreeMap;


@JmxResource(domainName = "entitas.gdx.monitor", description = "Entitas Gdx Debug JMX Monitor")
public class MonitorSystem implements IInitializeSystem {

    private Gson gson;
    private final EngineGDX engine;
    private final Entitas entitas;

    public MonitorSystem(Entitas entitas, EngineGDX engine) {
        this.engine = engine;
        this.entitas = entitas;
        this.gson = new Gson();

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

    @JmxAttributeField(description = "Indicates the context in which the Entity will be searched", isWritable = true)
    private int context;

    @JmxAttributeField(description = "Sets the index by which the entity will be searched", isWritable = true)
    private int indexEntity;

    @JmxAttributeField(description = "Sets the index by which the componet will be searched", isWritable = true)
    private int indexComponent;

    @JmxAttributeMethod(description = "get Components indices")
    public int[] getComponentIndices() {
        return entitas.allContexts()[context].getEntities()[indexEntity].getComponentIndices();
    }

    @JmxAttributeMethod(description = "get Game Components Lookup")
    public String getGameComponentLookup() {
        return gson.toJson(new GameComponentsLookup());
    }

    @JmxAttributeMethod(description = "get Component")
    public String getComponent() {
        String value = "";
        try {
            value = GameComponentsLookup.componentNames()[indexComponent] + " : ";
            if (entitas.allContexts()[context].getEntities()[indexEntity].hasComponent(indexComponent)) {
                value += gson.toJson(entitas.allContexts()[context].getEntities()[indexEntity].getComponent(indexComponent));
            } else {
                value += "Nulo";
            }

        } catch (Exception ex) {
            value = ex.getMessage();
            System.out.println(ex.getMessage());
        }
        //return  gson.toJson(entitas.game.getEntities()[indexEntity].getComponent(indexComponent));
        return value;
    }

    @JmxAttributeMethod(description = "get Components names")
    public String[] getComponentNames() {
        IComponent[] components = entitas.allContexts()[context].getEntities()[indexEntity].getComponents();
        String[] componentNames = new String[components.length];
        for (int i = 0; i < components.length; i++) {
            componentNames[i] = components[i].getClass().getSimpleName();
        }
        return componentNames;
    }


    @JmxAttributeMethod(description = "Game Components")
    public Map<String, Integer> getGameComponents() {
        Map<String, Integer> components = new TreeMap<>();
        for (int i = 0; i < GameComponentsLookup.componentNames().length; i++) {
            components.put(GameComponentsLookup.componentNames()[i], i);
        }
        return components;
    }

    @JmxAttributeMethod(description = "Sensor Components")
    public Map<String, Integer> getSensorComponents() {
        Map<String, Integer> components = new TreeMap<>();
        for (int i = 0; i < SensorComponentsLookup.componentNames().length; i++) {
            components.put(SensorComponentsLookup.componentNames()[i], i);
        }
        return components;
    }

    @JmxAttributeMethod(description = "Actuator Components")
    public Map<String, Integer> getActuatorComponents() {
        Map<String, Integer> components = new TreeMap<>();
        for (int i = 0; i < ActuatorComponentsLookup.componentNames().length; i++) {
            components.put(ActuatorComponentsLookup.componentNames()[i], i);
        }
        return components;
    }


    @JmxAttributeMethod(description = "get Entity")
    public Entity getEntity() {
        return entitas.allContexts()[context].getEntities()[indexEntity];
    }


}
