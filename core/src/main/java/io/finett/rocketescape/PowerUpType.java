package io.finett.rocketescape;

public enum PowerUpType {
    SHIELD("Shield", "Invulnerability", 5.0f, 0.3f, 0.5f, 1f),
    SLOW_TIME("Slow Time", "Slows obstacles", 8.0f, 1f, 0.8f, 0.3f),
    MAGNET("Magnet", "Attracts coins", 6.0f, 1f, 1f, 0.3f),
    DOUBLE_POINTS("2x Points", "Double score", 10.0f, 1f, 0.84f, 0f);

    private final String name;
    private final String description;
    private final float duration;
    private final float colorR;
    private final float colorG;
    private final float colorB;

    PowerUpType(String name, String description, float duration, float r, float g, float b) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getDuration() {
        return duration;
    }

    public float getColorR() {
        return colorR;
    }

    public float getColorG() {
        return colorG;
    }

    public float getColorB() {
        return colorB;
    }
}
