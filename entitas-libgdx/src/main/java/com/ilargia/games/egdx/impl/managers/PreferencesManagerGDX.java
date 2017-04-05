package com.ilargia.games.egdx.impl.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.ilargia.games.egdx.api.managers.PreferencesManager;

public class PreferencesManagerGDX implements PreferencesManager {

    public String APP_NAME = "";
    public int LOG_LEVEL = 0;
    public float GAME_HEIGHT = 16.875f; // 1080 / 64 =16.875 px
    public float GAME_WIDTH = 30f;     //  1920 / 64 = 30f px
    public int VIRTUAL_DEVICE_WIDTH = 250;     //  30 x 64 = 1920 px
    public int VIRTUAL_DEVICE_HEIGHT = 120; // 16.875 x 64 =1080 px


    // Box2D config
    public int MAX_FPS = 30;
    public int MIN_FPS = 15;
    public float TIME_STEP = 1f / MAX_FPS;
    public float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
    public float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
    public int VELOCITY_ITERATIONS = 10;
    public int POSITION_ITERATIONS = 6;

    //Box2DLight
    public boolean GAMMA_CORRECTION = false;     // enable or disable gamma correction
    public boolean USE_DIFFUSE_LIGHT = false;       // enable or disable diffused lighting
    public boolean BLUR = true;           // enabled or disable blur
    public int BLUR_NUM = 5;           // set number of gaussian blur passes
    public boolean SHADOWS = true;        // enable or disable shadow
    public boolean CULLING = true;        // enable or disable culling
    public Color AMBIENT_LIGHT = Color.CLEAR;

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

    public PreferencesManagerGDX() {
        try {
            preferences = Gdx.app.getPreferences("PREFS_NAME");
            if(preferences != null) load();
        } catch (Exception ex) {

        }

    }

    @Override
    public void initialize() { }


    @Override
    public void load() {
        if(preferences != null) {
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
            GAME_HEIGHT = preferences.getFloat(Constants.PREF_GAME_HEIGHT, 16.875F); // 1080 / 64 =16.875 px
            GAME_WIDTH = preferences.getFloat(Constants.PREF_GAME_WIDTH, 30F); // 1920 / 64 = 30f px
            VIRTUAL_DEVICE_HEIGHT = preferences.getInteger(Constants.PREF_VIRTUAL_DEVICE_HEIGHT, 1080); // 16.875 x 64 =1080 px
            VIRTUAL_DEVICE_WIDTH = preferences.getInteger(Constants.PREF_VIRTUAL_DEVICE_WIDTH, 1920); //  30 x 64 = 1920 px
            MAX_FPS = preferences.getInteger(Constants.PREF_MAX_FPS, 30);
            MIN_FPS = preferences.getInteger(Constants.PREF_MIN_FPS, 15);
            VELOCITY_ITERATIONS = preferences.getInteger(Constants.PREF_VELOCITY_ITERATIONS, 10); //  30 x 64 = 1920 px
            POSITION_ITERATIONS = preferences.getInteger(Constants.PREF_POSITION_ITERATIONS, 8); //  30 x 64 = 1920 px

            //Box2DLigth
            GAMMA_CORRECTION = preferences.getBoolean(Constants.PREF_GAMMA_CORRECTION, true);
            USE_DIFFUSE_LIGHT = preferences.getBoolean(Constants.PREF_USE_DIFFUSE_LIGHT, false);
            BLUR = preferences.getBoolean(Constants.PREF_BLUR, true);
            BLUR_NUM = preferences.getInteger(Constants.PREF_BLUR_NUM, 1);
            SHADOWS = preferences.getBoolean(Constants.PREF_SHADOWS, true);
            CULLING = preferences.getBoolean(Constants.PREF_CULLING, true);
           // AMBIENT_LIGHT = preferences.get(Constants.PREF_AMBIENT_LIGHT, 0.9F);

        }
    }

    @Override
    public void save() {
        if(preferences != null) {
            preferences.putBoolean(Constants.PREF_SOUND_ENABLED, SOUND);
            preferences.putBoolean(Constants.PREF_MUSIC_ENABLED, MUSIC);
            preferences.putFloat(Constants.PREF_VOLUME_SOUND, VOL_SOUND);
            preferences.putFloat(Constants.PREF_VOLUME_MUSIC, VOL_MUSIC);
            preferences.putBoolean(Constants.PREF_TOUCHPAD_ENABLED, TOUCH_PAD_ENABLED);
            preferences.flush();
            load();
        }
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
        public static final String PREF_GAME_HEIGHT = "pref.game.heigh";
        public static final String PREF_GAME_WIDTH = "pref.game.width";
        public static final String PREF_VIRTUAL_DEVICE_HEIGHT = "pref.virtual.device.heigh";
        public static final String PREF_VIRTUAL_DEVICE_WIDTH = "pref.virtual.device.width";
        // Box2D config
        public static final String PREF_MAX_FPS = "pref.max.fps";
        public static final String PREF_MIN_FPS = "pref.min.fps";
        public static final String PREF_VELOCITY_ITERATIONS = "pref.box2d.velocity.iterations";
        public static final String PREF_POSITION_ITERATIONS = "pref.box2d.position.iterations";

        //Box2DLigth
        public static final String PREF_GAMMA_CORRECTION = "pref.box2d.ligth.gamma.correction";     // enable or disable gamma correction
        public static final String PREF_USE_DIFFUSE_LIGHT = "pref.box2d.ligth.use.diffuse.ligth";       // enable or disable diffused lighting
        public static final String PREF_BLUR = "pref.box2d.ligth.blur";           // enabled or disable blur
        public static final String PREF_BLUR_NUM = "pref.box2d.ligth.blur.num";           // set number of gaussian blur passes
        public static final String PREF_SHADOWS = "pref.box2d.ligth.shadows";        // enable or disable shadow
        public static final String PREF_CULLING = "pref.box2d.ligth.culling";        // enable or disable culling
        public static final String PREF_AMBIENT_LIGHT = "pref.box2d.ligth";


    }
}
