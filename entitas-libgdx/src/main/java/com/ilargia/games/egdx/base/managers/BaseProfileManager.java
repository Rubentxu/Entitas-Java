package com.ilargia.games.egdx.base.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.ilargia.games.egdx.api.managers.ProfileManager;

public class BaseProfileManager<P> implements ProfileManager<P> {

    private BasePreferencesManager preferencesManager;
    private P profile;

    public BaseProfileManager(P profile, BasePreferencesManager preferencesManager) {
        this.profile = profile;
        this.preferencesManager = preferencesManager;
    }

    @Override
    public P retrieveProfile() {
        FileHandle profileDataFile = Gdx.files.local(preferencesManager.PROFILE_DATA_FILE);
        Json json = new Json();

        if (profileDataFile.exists()) {
            try {
                String profileAsText = profileDataFile.readString().trim();

                if (profileAsText.matches("^[A-Za-z0-9/+=]+$")) {
                    profileAsText = Base64Coder.decodeString(profileAsText);
                }
                profile = (P) json.fromJson(profile.getClass(), profileAsText);

            } catch (Exception e) {
                FileHandle initProfileDataFile = Gdx.files.internal(preferencesManager.INIT_PROFILE_DATA_FILE);
                profile = (P) json.fromJson(profile.getClass(), initProfileDataFile.readString().trim());
                persist(profile);
            }
        } else {
            FileHandle initProfileDataFile = Gdx.files.internal(preferencesManager.INIT_PROFILE_DATA_FILE);
            profile = (P) json.fromJson(profile.getClass(), initProfileDataFile.readString().trim());
            persist(profile);
        }
        return profile;

    }

    @Override
    public void persist(P profile) {
        FileHandle profileDataFile = Gdx.files.local(preferencesManager.PROFILE_DATA_FILE);
        Json json = new Json();
        String profileAsText = json.toJson(profile);

        profileAsText = Base64Coder.encodeString(profileAsText);
        profileDataFile.writeString(profileAsText, false);
    }

    @Override
    public void resetToDefaultProfile() {
        FileHandle profileDataFile = Gdx.files.local(preferencesManager.PROFILE_DATA_FILE);
        if (profileDataFile.exists()) profileDataFile.delete();
    }

    @Override
    public P getProfile() {
        if (profile == null) return retrieveProfile();
        return profile;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void dispose() {

    }
}
