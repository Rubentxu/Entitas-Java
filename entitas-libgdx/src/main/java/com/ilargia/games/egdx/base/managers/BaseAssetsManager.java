package com.ilargia.games.egdx.base.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.ilargia.games.egdx.api.managers.*;

public class BaseAssetsManager implements TextureManager<Texture, TextureAtlas>, FontManager<BitmapFont>,
        MusicManager<Music>, SoundManager<Sound> {

    private AssetManager assetManager;
    private BasePreferencesManager preferencesManager;
    private Music currentMusicPlaying;
    private Sound soundToPlay;

    public BaseAssetsManager(AssetManager assetManager, BasePreferencesManager preferencesManager) {
        this.assetManager = assetManager;
        this.preferencesManager = preferencesManager;

    }

    public void loadAsset(String fileName, Class<?> id) {
        if (!assetManager.isLoaded(fileName))
            assetManager.load(fileName, id);
    }

    public TiledMap getMap(String name) {
        return assetManager.get(name);
    }

    @Override
    public void loadTexture(String name) {
        loadAsset(name, Texture.class);
    }

    @Override
    public void unloadTexture(String name) {
        assetManager.unload(name);
    }

    @Override
    public Texture getTexture(String name) {
        return assetManager.get(name, Texture.class);

    }

    @Override
    public void loadTextureAtlas(String name) {
        loadAsset(name, TextureAtlas.class);
    }

    @Override
    public void unloadTextureAtlas(String name) {
        assetManager.unload(name);
    }

    @Override
    public TextureAtlas getTextureAtlas(String name) {
        return assetManager.get(name, TextureAtlas.class);
    }

    @Override
    public void loadFont(String name) {
        loadAsset(name, BitmapFont.class);
    }

    @Override
    public void unloadFont(String name) {
        assetManager.unload(name);
    }

    @Override
    public BitmapFont getFont(String name) {
        return assetManager.get(name, BitmapFont.class);
    }


    @Override
    public void loadMusic(String name) {
        loadAsset(name, Music.class);
    }

    @Override
    public void unloadMusic(String name) {
        assetManager.unload(name);
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
    public void loadSound(String name) {
        loadAsset(name, Sound.class);
    }

    @Override
    public void unloadSound(String name) {
        assetManager.unload(name);
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
    public void initialize() {

    }

    public void dispose() {
        assetManager.dispose();
    }

    public void finishLoading() {
        assetManager.finishLoading();
    }

}
