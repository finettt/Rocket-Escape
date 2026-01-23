package io.finett.rocketescape;

public enum Achievement {
    FIRST_FLIGHT("First Flight", "Play your first game", 0),
    COMBO_MASTER_10("Combo Master", "Reach 10x combo", 10),
    COMBO_MASTER_25("Combo Legend", "Reach 25x combo", 25),
    SURVIVOR_50("Survivor", "Score 50 points", 50),
    SURVIVOR_100("Century", "Score 100 points", 100),
    SURVIVOR_200("Double Century", "Score 200 points", 200),
    NO_DAMAGE("Perfect Flight", "Score 30 without taking damage", 30),
    SPEED_DEMON("Speed Demon", "Survive 2.0x difficulty", 0),
    POWERUP_COLLECTOR("Collector", "Collect 10 power-ups", 10),
    HARDCORE_SURVIVOR("Hardcore Hero", "Score 50 in one life", 50);

    private final String name;
    private final String description;
    private final int requirement;

    Achievement(String name, String description, int requirement) {
        this.name = name;
        this.description = description;
        this.requirement = requirement;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRequirement() {
        return requirement;
    }
}
