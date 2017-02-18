package com.ilargia.games.egdx.base.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.ilargia.games.egdx.api.managers.PreferencesManager;

public class BasePreferencesManager implements PreferencesManager {

    public String APP_NAME = "";
    public String LOG = "";
    public float WORLD_HEIGHT = 16.875f; // 1080 / 64 =16.875 px
    public float WORLD_WIDTH = 30f;     //  1920 / 64 = 30f px
    public float VIRTUAL_HEIGHT = 1080f; // 16.875 x 64 =1080 px
    public float VIRTUAL_WIDTH = 1920f;     //  30 x 64 = 1920 px
    // Box2D config
    public float RUNNING_FRAME_DURATION = 0.02f;
    public int VELOCITY_ITERATIONS = 10;
    public int POSITION_ITERATIONS = 8;
    // Preferences
    public boolean SOUND = false;
    public boolean MUSIC = false;
    public float VOL_SOUND = 0.5f;
    public float VOL_MUSIC = 0.5f;
    public boolean TOUCH_PAD_ENABLED = true;

    // Profile
    public String PROFILE_DATA_FILE = "data/profile.game";
    public String INIT_PROFILE_DATA_FILE = "data/initProfile.game";
    private Preferences preferences;

    public BasePreferencesManager() {
        preferences = Gdx.app.getPreferences("PREFS_NAME");
        load();
    }

    @Override
    public void load() {
        APP_NAME = preferences.getString(Constants.PREFS_NAME, "TEST_GAME");
        SOUND = preferences.getBoolean(Constants.PREF_SOUND_ENABLED, true);
        MUSIC = preferences.getBoolean(Constants.PREF_MUSIC_ENABLED, true);
        VOL_SOUND = MathUtils.clamp(preferences.getFloat(Constants.PREF_VOLUME_SOUND, 0.5f),
                0.0f, 1.0f);
        VOL_MUSIC = MathUtils.clamp(preferences.getFloat(Constants.PREF_VOLUME_MUSIC, 0.5f),
                0.0f, 1.0f);
        TOUCH_PAD_ENABLED = preferences.getBoolean(Constants.PREF_TOUCHPAD_ENABLED, true);
        PROFILE_DATA_FILE = preferences.getString(Constants.PREF_PROFILE_DATA_FILE, "data/profile.game");
        INIT_PROFILE_DATA_FILE = preferences.getString(Constants.PREF_INIT_PROFILE_DATA_FILE, "data/initProfile.game");
        LOG = APP_NAME;
        WORLD_HEIGHT = preferences.getFloat(Constants.PREF_WORLD_HEIGHT, 16.875F); // 1080 / 64 =16.875 px
        WORLD_WIDTH = preferences.getFloat(Constants.PREF_WORLD_WIDTH, 30F); // 1920 / 64 = 30f px
        VIRTUAL_HEIGHT = preferences.getFloat(Constants.PREF_VIRTUAL_HEIGHT, 1080F); // 16.875 x 64 =1080 px
        VIRTUAL_WIDTH = preferences.getFloat(Constants.PREF_VIRTUAL_WIDTH, 1920F); //  30 x 64 = 1920 px
        RUNNING_FRAME_DURATION = preferences.getFloat(Constants.PREF_RUNNING_FRAME_DURATION, 0.02F); //  30 x 64 = 1920 px
        VELOCITY_ITERATIONS = preferences.getInteger(Constants.PREF_VELOCITY_ITERATIONS, 10); //  30 x 64 = 1920 px
        POSITION_ITERATIONS = preferences.getInteger(Constants.PREF_POSITION_ITERATIONS, 8); //  30 x 64 = 1920 px

    }

    @Override
    public void save() {
        preferences.putBoolean(Constants.PREF_SOUND_ENABLED, SOUND);
        preferences.putBoolean(Constants.PREF_MUSIC_ENABLED, MUSIC);
        preferences.putFloat(Constants.PREF_VOLUME_SOUND, VOL_SOUND);
        preferences.putFloat(Constants.PREF_VOLUME_MUSIC, VOL_MUSIC);
        preferences.putBoolean(Constants.PREF_TOUCHPAD_ENABLED, TOUCH_PAD_ENABLED);
        preferences.flush();
        load();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void dispose() {

    }

    private class Constants {

        public static final String PREFS_NAME = "pref.game.name";
        public static final String PREF_VOLUME_SOUND = "pref.sound.volume";
        public static final String PREF_SOUND_ENABLED = "pref.sound.enabled";
        public static final String PREF_VOLUME_MUSIC = "pref.music.volume";
        public static final String PREF_MUSIC_ENABLED = "pref.music.enabled";

        public static final String PREF_TOUCHPAD_ENABLED = "pref.touchpad.enabled";
        public static final String PREF_PROFILE_DATA_FILE = "pref.profile.file";
        public static final String PREF_INIT_PROFILE_DATA_FILE = "pref.profile.init.file";
        public static final String PREF_WORLD_HEIGHT = "pref.world.heigh";
        public static final String PREF_WORLD_WIDTH = "pref.world.width";
        public static final String PREF_VIRTUAL_HEIGHT = "pref.world.virtual.heigh";
        public static final String PREF_VIRTUAL_WIDTH = "pref.world.virtual.width";
        // Box2D config
        public static final String PREF_RUNNING_FRAME_DURATION = "pref.running.frame.duration";
        public static final String PREF_VELOCITY_ITERATIONS = "pref.box2d.velocity.iterations";
        public static final String PREF_POSITION_ITERATIONS = "pref.box2d.position.iterations";


    }
}
