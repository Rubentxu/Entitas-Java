package com.ilargia.games.egdx.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.ilargia.games.egdx.interfaces.managers.PreferencesManager;

public class EGPreferencesManager implements PreferencesManager {

    private Preferences preferences;
    public boolean sound = false;
    public boolean music = false;
    public float volSound = 0.5f;
    public float volMusic = 0.5f;
    public boolean touchPadEnabled = true;

    public EGPreferencesManager() {
        preferences = Gdx.app.getPreferences("PREFS_NAME");
        load();
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}
