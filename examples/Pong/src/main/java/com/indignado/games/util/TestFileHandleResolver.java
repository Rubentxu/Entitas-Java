package com.indignado.games.util;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.net.URL;

public class TestFileHandleResolver implements FileHandleResolver {

    @Override
    public FileHandle resolve(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        File file = new File(url.getPath());
        return new FileHandle(file);

    }

}