package com.ilargia.games.egdx.managers;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.ilargia.games.egdx.interfaces.managers.SoundManager;

public class EGSoundManager implements SoundManager<Class<Sound>, Sound> {

    private AssetManager assetManager;
    private EGPreferencesManager preferencesManager;
    private Sound soundToPlay;

    public EGSoundManager(AssetManager assetManager, EGPreferencesManager preferencesManager) {
        this.assetManager = assetManager;
        this.preferencesManager = preferencesManager;
    }

    @Override
    public void playSound(String name) {
        if (!preferencesManager.SOUND) {
            return;
        }
        soundToPlay = getAsset(name, Sound.class);
        if (soundToPlay != null) {
            soundToPlay.play(preferencesManager.VOL_SOUND);
        }
    }

    @Override
    public <A> void loadAsset(String fileName, Class<Sound> id, A... args) {
        if (!assetManager.isLoaded(fileName))
            assetManager.get(fileName, id);
    }

    @Override
    public <V> void unloadAsset(String fileName) {
        assetManager.unload(fileName);
    }

    @Override
    public Sound getAsset(String name, Class<Sound> id) {
        return (Sound) assetManager.get(name, id);
    }


    @Override
    public void dispose() {

    }
}
