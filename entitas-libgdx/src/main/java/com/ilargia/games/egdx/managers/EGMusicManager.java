package com.ilargia.games.egdx.managers;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.ilargia.games.egdx.interfaces.managers.MusicManager;

public class EGMusicManager implements MusicManager<Class<Music>, Music> {

    private AssetManager assetManager;
    EGPreferencesManager preferencesManager;
    private Music currentMusicPlaying;

    public EGMusicManager(com.badlogic.gdx.assets.AssetManager assetManager, EGPreferencesManager preferencesManager) {
        this.assetManager = assetManager;
        this.preferencesManager = preferencesManager;
    }

    @Override
    public void playMusic(String name) {
        if (!preferencesManager.MUSIC) {
            stopMusic();
            return;
        }
        stopMusic();
        if (assetManager.isLoaded(name, Music.class))
            currentMusicPlaying = assetManager.get(name, Music.class);
        loadAsset(name, Music.class);
        assetManager.finishLoading();

        if (currentMusicPlaying != null) {
            currentMusicPlaying.play();
            currentMusicPlaying.setLooping(true);
            currentMusicPlaying.setVolume(preferencesManager.VOL_MUSIC);
        }
    }

    @Override
    public void stopMusic() {
        if (currentMusicPlaying != null) {
            currentMusicPlaying.stop();
            String assetFileName = assetManager.getAssetFileName(currentMusicPlaying);
            assetManager.unload(assetFileName);
            currentMusicPlaying = null;
        }
    }

    @Override
    public <A> void loadAsset(String fileName, Class<Music> id, A... args) {
        assetManager.load(fileName, id, null);
    }

    @Override
    public <V> void unloadAsset(String fileName) {
        assetManager.unload(fileName);
    }

    @Override
    public Music getAsset(String name, Class<Music> id) {
        return (Music) assetManager.get(name, id);
    }


    @Override
    public void dispose() {

    }
}
