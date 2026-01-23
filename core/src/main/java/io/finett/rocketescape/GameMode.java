package io.finett.rocketescape;

public enum GameMode {
    CLASSIC("Classic", "Standard gameplay with 3 lives", 1.0f, 3),
    HARDCORE("Hardcore", "One life, faster start, x1.5 score", 1.5f, 1),
    ZEN("Zen Mode", "No death, infinite flight", 1.0f, 999),
    TIME_ATTACK("Time Attack", "60 seconds to score high", 1.3f, 3);

    private final String displayName;
    private final String description;
    private final float startingDifficulty;
    private final int lives;

    GameMode(String displayName, String description, float startingDifficulty, int lives) {
        this.displayName = displayName;
        this.description = description;
        this.startingDifficulty = startingDifficulty;
        this.lives = lives;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public float getStartingDifficulty() {
        return startingDifficulty;
    }

    public int getLives() {
        return lives;
    }
}
