package com.ilargia.games.egdx.util;


import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.ilargia.games.egdx.api.EntityFactory;
import com.ilargia.games.egdx.base.managers.BaseSceneManager;
import net.dermetfan.gdx.maps.MapUtils;

import java.util.Iterator;

public class MapEntityParser {
    private BaseSceneManager baseSceneManager;
    private Aliases aliases = new Aliases();
    private float unitScale;
    private boolean ignoreMapUnitScale;
    private boolean ignoreLayerUnitScale;
    private float tileWidth;
    private float tileHeight;
    private boolean triangulate;
    private MapProperties heritage;
    private MapProperties mapProperties;
    private MapProperties layerProperties;
    private final Vector2 vec2;

    public MapEntityParser(BaseSceneManager baseSceneManager) {
        this.unitScale = 1.0F;
        this.tileWidth = 1.0F;
        this.tileHeight = 1.0F;
        this.vec2 = new Vector2();
        this.baseSceneManager = baseSceneManager;
    }


    public World load(World world, Map map) {
        MapProperties oldMapProperties = this.mapProperties;
        this.mapProperties = map.getProperties();
        world.setGravity(this.vec2.set(((Float) MapUtils.getProperty(this.mapProperties, this.aliases.gravityX, Float.valueOf(world.getGravity().x))).floatValue(), ((Float) MapUtils.getProperty(this.mapProperties, this.aliases.gravityY, Float.valueOf(world.getGravity().y))).floatValue()));
        world.setAutoClearForces(((Boolean) MapUtils.getProperty(this.mapProperties, this.aliases.autoClearForces, Boolean.valueOf(world.getAutoClearForces()))).booleanValue());
        if (!this.ignoreMapUnitScale) {
            this.unitScale = ((Float) MapUtils.getProperty(this.mapProperties, this.aliases.unitScale, Float.valueOf(this.unitScale))).floatValue();
        }

        this.tileWidth = ((Float) MapUtils.getProperty(this.mapProperties, this.aliases.tileWidth, Float.valueOf(this.tileWidth))).floatValue();
        this.tileHeight = ((Float) MapUtils.getProperty(this.mapProperties, this.aliases.tileHeight, Float.valueOf(this.tileHeight))).floatValue();

        Array layers = (Array) Pools.obtain(Array.class);
        layers.clear();
        Iterator var5 = layers.iterator();

        while (var5.hasNext()) {
            MapLayer mapLayer = (MapLayer) var5.next();
            this.load(world, mapLayer);
        }

        layers.clear();
        Pools.free(layers);
        this.mapProperties = oldMapProperties;
        return world;
    }

    public World load(World world, MapLayer layer) {
        MapProperties oldLayerProperties = this.layerProperties;
        this.layerProperties = layer.getProperties();
        float oldUnitScale = this.unitScale;
        if (!this.ignoreLayerUnitScale) {
            this.unitScale = ((Float) MapUtils.getProperty(layer.getProperties(), this.aliases.unitScale, Float.valueOf(this.unitScale))).floatValue();
        }

        String typeFallback = (String) MapUtils.findProperty(this.aliases.type, "", new MapProperties[]{this.heritage, this.mapProperties, this.layerProperties});
        Array objects = (Array) Pools.obtain(Array.class);
        objects.clear();
        Iterator var7 = objects.iterator();

        MapObject object;
        String type;
        while (var7.hasNext()) {
            object = (MapObject) var7.next();
            type = (String) MapUtils.getProperty(object.getProperties(), this.aliases.type, typeFallback);
            if (type.equals(this.aliases.entity)) {
                baseSceneManager.createEntity(aliases.entity);
            }
        }


        objects.clear();
        Pools.free(objects);
        this.layerProperties = oldLayerProperties;
        this.unitScale = oldUnitScale;
        return world;
    }


    public void reset() {
        this.aliases = new Aliases();
        this.unitScale = 1.0F;
        this.tileWidth = 1.0F;
        this.tileHeight = 1.0F;
        this.triangulate = false;
        this.heritage = null;
        this.mapProperties = null;
        this.layerProperties = null;
    }

    public float getUnitScale() {
        return this.unitScale;
    }

    public void setUnitScale(float unitScale) {
        this.unitScale = unitScale;
    }

    public boolean isIgnoreMapUnitScale() {
        return this.ignoreMapUnitScale;
    }

    public void setIgnoreMapUnitScale(boolean ignoreMapUnitScale) {
        this.ignoreMapUnitScale = ignoreMapUnitScale;
    }

    public boolean isIgnoreLayerUnitScale() {
        return this.ignoreLayerUnitScale;
    }

    public void setIgnoreLayerUnitScale(boolean ignoreLayerUnitScale) {
        this.ignoreLayerUnitScale = ignoreLayerUnitScale;
    }

    public float getTileWidth() {
        return this.tileWidth;
    }

    public void setTileWidth(float tileWidth) {
        this.tileWidth = tileWidth;
    }

    public float getTileHeight() {
        return this.tileHeight;
    }

    public void setTileHeight(float tileHeight) {
        this.tileHeight = tileHeight;
    }

    public boolean isTriangulate() {
        return this.triangulate;
    }

    public void setTriangulate(boolean triangulate) {
        this.triangulate = triangulate;
    }

    public Aliases getAliases() {
        return this.aliases;
    }


    public static class Aliases {
        public String x = "x";
        public String y = "y";
        public String width = "width";
        public String height = "height";
        public String type = "type";
        public String entity= "entity";
        public String gravityX= "gravityX";
        public String gravityY= "gravityY";
        public String autoClearForces = "autoClearForces";
        public String unitScale = "unitScale";
        public String tileWidth = "tileWidth";
        public String tileHeight = "tileHeight";

        public Aliases() {
        }
    }
}
