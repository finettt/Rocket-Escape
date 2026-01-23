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
    private static final String PREF_TOTAL_POWERUPS = "total_powerups";
    private static final String PREF_GAMES_PLAYED = "games_played";
    private static final String PREF_MAX_COMBO_EVER = "max_combo_ever";
    private static final String PREF_MAX_SCORE_NO_DAMAGE = "max_score_no_damage";
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
    public void incrementPowerupsCollected() {
        int current = preferences.getInteger(PREF_TOTAL_POWERUPS, 0);
        preferences.putInteger(PREF_TOTAL_POWERUPS, current + 1);
        preferences.flush();
    }

    public int getTotalPowerups() {
        return preferences.getInteger(PREF_TOTAL_POWERUPS, 0);
    }
    // Achievement tracking methods
    public void incrementGamesPlayed() {
        int current = preferences.getInteger(PREF_GAMES_PLAYED, 0);
        preferences.putInteger(PREF_GAMES_PLAYED, current + 1);
        preferences.flush();
    }

    public int getGamesPlayed() {
        return preferences.getInteger(PREF_GAMES_PLAYED, 0);
    }

    public void updateMaxCombo(int combo) {
        if (combo > getMaxComboEver()) {
            preferences.putInteger(PREF_MAX_COMBO_EVER, combo);
            preferences.flush();
        }
    }

    public int getMaxComboEver() {
        return preferences.getInteger(PREF_MAX_COMBO_EVER, 0);
    }

    public void updateMaxScoreNoDamage(int score) {
        if (score > getMaxScoreNoDamage()) {
            preferences.putInteger(PREF_MAX_SCORE_NO_DAMAGE, score);
            preferences.flush();
        }
    }

    public int getMaxScoreNoDamage() {
        return preferences.getInteger(PREF_MAX_SCORE_NO_DAMAGE, 0);
    }

    public boolean isAchievementUnlocked(Achievement achievement) {
        return preferences.getBoolean("achievement_" + achievement.name(), false);
    }

    public void unlockAchievement(Achievement achievement) {
        if (!isAchievementUnlocked(achievement)) {
            preferences.putBoolean("achievement_" + achievement.name(), true);
            preferences.flush();
        }
    }

    public int getUnlockedAchievementsCount() {
        int count = 0;
        for (Achievement achievement : Achievement.values()) {
            if (isAchievementUnlocked(achievement)) {
                count++;
            }
        }
        return count;
    }
    public void resetAllAchievements() {
        for (Achievement achievement : Achievement.values()) {
            preferences.putBoolean("achievement_" + achievement.name(), false);
        }
        // Also reset tracking stats
        preferences.putInteger(PREF_GAMES_PLAYED, 0);
        preferences.putInteger(PREF_MAX_COMBO_EVER, 0);
        preferences.putInteger(PREF_MAX_SCORE_NO_DAMAGE, 0);
        preferences.putInteger(PREF_TOTAL_POWERUPS, 0);
        preferences.flush();
    }
    @Override
    public void dispose() {
        super.dispose();
    }
}
