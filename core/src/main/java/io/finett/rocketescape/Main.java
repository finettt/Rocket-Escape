package io.finett.rocketescape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private Preferences preferences;

    private static final String PREFS_NAME = "rocket_escape_prefs";
    private static final String PREF_HIGH_SCORE = "high_score";
    private static final String PREF_SOUND_ENABLED = "sound_enabled";
    private static final String PREF_MUSIC_ENABLED = "music_enabled";

    @Override
    public void create() {
        preferences = Gdx.app.getPreferences(PREFS_NAME);
        setScreen(new MainMenuScreen(this));
    }

    public int getHighScore() {
        return preferences.getInteger(PREF_HIGH_SCORE, 0);
    }

    public void setHighScore(int score) {
        if (score > getHighScore()) {
            preferences.putInteger(PREF_HIGH_SCORE, score);
            preferences.flush();
        }
    }

    public boolean isSoundEnabled() {
        return preferences.getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        preferences.putBoolean(PREF_SOUND_ENABLED, enabled);
        preferences.flush();
    }

    public boolean isMusicEnabled() {
        return preferences.getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean enabled) {
        preferences.putBoolean(PREF_MUSIC_ENABLED, enabled);
        preferences.flush();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
