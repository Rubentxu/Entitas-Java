package com.ilargia.games.egdx.impl.managers;

import com.badlogic.gdx.Gdx;
import com.ilargia.games.egdx.api.managers.LogManager;

public class LogManagerGDX implements LogManager {

    @FunctionalInterface
    interface Log  {
        public void trace (int level, String tag, String message, Object... args);
    }

    private PreferencesManagerGDX preferencesManager;
    private static Log log;

    public LogManagerGDX(PreferencesManagerGDX preferencesManager) {
        this.preferencesManager = preferencesManager;

    }

    @Override
    public void initialize() {
        if(preferencesManager.LOG_LEVEL > LOG_NONE) {
            if(Gdx.app != null) {
                Gdx.app.setLogLevel(preferencesManager.LOG_LEVEL);
                log = (int level, String tag, String message, Object... args)-> {
                    if(level>= preferencesManager.LOG_LEVEL ) {
                        switch (level) {
                            case LOG_ERROR:
                                Gdx.app.error(tag, String.format(message, args));
                                break;
                            case LOG_INFO:
                                Gdx.app.log(tag, String.format(message, args));
                                break;
                            case LOG_DEBUG:
                                Gdx.app.debug(tag, String.format(message, args));
                        }
                    }

                };
            } else {
                log = (int level, String tag, String message, Object... args)-> {
                    if(level>= preferencesManager.LOG_LEVEL ) {
                        switch (level) {
                            case LOG_ERROR:
                                System.err.println(String.format("%s > %s",tag, String.format(message, args)));
                                break;
                            case LOG_INFO:
                                System.out.println(String.format("%s > %s",tag, String.format(message, args)));
                                break;
                            case LOG_DEBUG:
                                System.out.println(String.format("%s > %s",tag, String.format(message, args)));
                        }
                    }

                };
            }
        } else {
            log = (int level, String tag, String message, Object... args)-> { };
        }
    }


    public static void debug(String tag, String message, Object ...args) {
        log.trace(LOG_DEBUG, tag, message, args);
    }


    public static void info(String tag, String message, Object... args) {
        log.trace(LOG_INFO, tag, message, args);

    }

    public static void error(String tag, String message, Object... args) {
        log.trace(LOG_ERROR, tag, message, args);

    }

    @Override
    public void dispose() {

    }

}
