package com.ilargia.games.egdx.managers;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ilargia.games.egdx.base.interfaces.managers.*;

public class EGAssetsManager implements AssetManager<Class<?>>, TextureManager<Texture>, FontManager<BitmapFont>,
        MusicManager<Music>, SoundManager<Sound> {

    private com.badlogic.gdx.assets.AssetManager assetManager;
    private EGPreferencesManager preferencesManager;
    private Music currentMusicPlaying;
    private Sound soundToPlay;

    public EGAssetsManager(com.badlogic.gdx.assets.AssetManager assetManager, EGPreferencesManager preferencesManager) {
        this.assetManager = assetManager;
        this.preferencesManager = preferencesManager;

    }

    @Override
    public void loadAsset(String fileName, Class<?> id) {
        if (!assetManager.isLoaded(fileName))
            assetManager.load(fileName, id);
    }

    @Override
    public void unloadAsset(String fileName) {
        assetManager.unload(fileName);
    }

    @Override
    public Texture getTexture(String name) {
        return assetManager.get(name, Texture.class);

    }

    @Override
    public BitmapFont getFont(String name) {
        return assetManager.get(name, BitmapFont.class);
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
    public Music getMusic(String fileName) {
        return assetManager.get(fileName, Music.class);
    }

    @Override
    public void playSound(String name) {
        if (!preferencesManager.SOUND) {
            return;
        }
        soundToPlay = getSount(name);
        if (soundToPlay != null) {
            soundToPlay.play(preferencesManager.VOL_SOUND);
        }
    }

    @Override
    public Sound getSount(String fileName) {
        return assetManager.get(fileName, Sound.class);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public void finishLoading() {
        assetManager.finishLoading();
    }

}
