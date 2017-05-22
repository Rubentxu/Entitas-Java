package ilargia.egdx.util;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import ilargia.egdx.impl.managers.SceneManagerGDX;

public class MapEntityParser {
    private TmxMapLoader loader;
    private SceneManagerGDX baseSceneManager;
    private Aliases aliases = new Aliases();


    public MapEntityParser(SceneManagerGDX baseSceneManager) {
        this.baseSceneManager = baseSceneManager;
        loader = new TmxMapLoader();
    }

    public void load(String scene) {
        TiledMap map = loader.load(scene);

        MapObjects characters = map.getLayers().get("Characters").getObjects();
        MapObjects statics = map.getLayers().get("Statics").getObjects();

        for (MapObject object : characters) {
            String type = object.getProperties().get(aliases.Character, "", String.class);
            if (type != null)
                baseSceneManager.createEntity(type);

        }

        for (MapObject object : statics) {
            String type = object.getProperties().get(aliases.Statics, "", String.class);
            if (type != null)
                baseSceneManager.createEntity(type);

        }

    }

    public void reset() {
        this.aliases = new Aliases();

    }

    public static class Aliases {
        public String Character = "Character";
        public String Statics = "Statics";

        public Aliases() {
        }
    }

}
