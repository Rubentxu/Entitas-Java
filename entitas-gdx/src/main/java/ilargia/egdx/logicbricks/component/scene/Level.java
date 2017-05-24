package ilargia.egdx.logicbricks.component.scene;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Scene"})
public class Level implements Json.Serializable {

    private String map;
    private String levelName;
    private String description;
    private String music;
    private boolean active = false;
    private int achievements = 0;
    private int highScore = 0;
    private int num = 0;

    public Level() {
    }

    public Level(String map, String levelName, String description, String music, String background_01, String background_02, String background_03,
                 boolean active, int achievements) {
        this.map = map;
        this.levelName = levelName;
        this.description = description;
        this.music = music;

        this.active = active;
        this.achievements = achievements;
    }

    public String getMap() {
        return map;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getDescription() {
        return description;
    }

    public String getMusic() {
        return music;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAchievements() {
        return achievements;
    }

    public void setAchievements(int achievements) {
        this.achievements = achievements;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

        map = json.readValue("map", String.class, jsonData);
        levelName = json.readValue("levelName", String.class, jsonData);
        description = json.readValue("description", String.class, jsonData);
        music = json.readValue("music", String.class, jsonData);
        active = json.readValue("active", Boolean.class, jsonData);
        achievements = json.readValue("achievements", Integer.class, jsonData);
        highScore = json.readValue("highScore", Integer.class, jsonData);
        num = json.readValue("num", Integer.class, jsonData);

    }

    @Override
    public void write(Json json) {
        json.writeValue("map", map);
        json.writeValue("levelName", levelName);
        json.writeValue("description", description);
        json.writeValue("music", music);
        json.writeValue("active", active);
        json.writeValue("achievements", achievements);
        json.writeValue("highScore", highScore);
        json.writeValue("num", num);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
