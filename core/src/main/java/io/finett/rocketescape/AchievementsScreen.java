package io.finett.rocketescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class AchievementsScreen implements Screen {
    private final Main game;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture background;
    private BitmapFont titleFont;
    private BitmapFont menuFont;
    private BitmapFont smallFont;

    private GlyphLayout titleLayout;
    private GlyphLayout backLayout;
    private GlyphLayout resetLayout;

    private Rectangle backButton;
    private Rectangle resetButton;
    private Rectangle[] achievementBoxes;

    private static final float BUTTON_WIDTH = 200f;
    private static final float BUTTON_HEIGHT = 50f;
    private static final float ACHIEVEMENT_BOX_WIDTH = 600f;
    private static final float ACHIEVEMENT_BOX_HEIGHT = 60f;
    private static final float ACHIEVEMENT_SPACING = 10f;
    private static final float START_Y = 160f;

    private float scrollOffset = 0f;
    private static final float SCROLL_SPEED = 300f;

    // Reset confirmation
    private boolean showResetConfirmation = false;
    private float confirmationTimer = 0f;
    private static final float CONFIRMATION_TIME = 3f;
    private Rectangle confirmYesButton;
    private Rectangle confirmNoButton;

    public AchievementsScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        background = new Texture("space-bg.png");

        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal("PressStart2P-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = 36;
            titleFont = generator.generateFont(parameter);

            parameter.size = 18;
            menuFont = generator.generateFont(parameter);

            parameter.size = 14;
            smallFont = generator.generateFont(parameter);
        } finally {
            if (generator != null) {
                generator.dispose();
            }
        }

        titleLayout = new GlyphLayout();
        backLayout = new GlyphLayout();
        resetLayout = new GlyphLayout();

        titleLayout.setText(titleFont, "ACHIEVEMENTS");
        backLayout.setText(menuFont, "BACK");
        resetLayout.setText(menuFont, "RESET ALL");

        float centerX = Gdx.graphics.getWidth() / 2f;

        backButton = new Rectangle(20, 20, BUTTON_WIDTH, BUTTON_HEIGHT);
        resetButton = new Rectangle(Gdx.graphics.getWidth() - BUTTON_WIDTH - 20, 20, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Create achievement boxes
        Achievement[] achievements = Achievement.values();
        achievementBoxes = new Rectangle[achievements.length];

        float boxX = centerX - ACHIEVEMENT_BOX_WIDTH / 2;
        float currentY = Gdx.graphics.getHeight() - START_Y;

        for (int i = 0; i < achievements.length; i++) {
            achievementBoxes[i] = new Rectangle(boxX, currentY, ACHIEVEMENT_BOX_WIDTH, ACHIEVEMENT_BOX_HEIGHT);
            currentY -= (ACHIEVEMENT_BOX_HEIGHT + ACHIEVEMENT_SPACING);
        }

        // Confirmation buttons
        confirmYesButton = new Rectangle(centerX - 220, Gdx.graphics.getHeight() / 2 - 80, 200, 50);
        confirmNoButton = new Rectangle(centerX + 20, Gdx.graphics.getHeight() / 2 - 80, 200, 50);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle scrolling
        if (Gdx.input.isTouched()) {
            // Simple touch scrolling could be added here
        }

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        boolean backHover = backButton.contains(touchX, touchY);
        boolean resetHover = !showResetConfirmation && resetButton.contains(touchX, touchY);

        // Background
        batch.begin();
        batch.setColor(0.2f, 0.2f, 0.2f, 1f);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1, 1, 1, 1);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Draw achievement boxes
        Achievement[] achievements = Achievement.values();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < achievements.length; i++) {
            Achievement achievement = achievements[i];
            boolean unlocked = game.isAchievementUnlocked(achievement);
            Rectangle box = achievementBoxes[i];

            if (unlocked) {
                shapeRenderer.setColor(1f, 0.84f, 0f, 0.7f); // Gold for unlocked
            } else {
                shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.7f); // Gray for locked
            }
            shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        // Draw buttons
        drawButton(backButton, backHover);
        if (!showResetConfirmation) {
            drawButton(resetButton, resetHover);
        }

        shapeRenderer.end();

        batch.begin();

        // Title
        titleFont.setColor(1f, 0.8f, 0f, 1f);
        titleFont.draw(batch, "ACHIEVEMENTS",
            Gdx.graphics.getWidth() / 2f - titleLayout.width / 2f,
            Gdx.graphics.getHeight() - 30);

        // Achievement count
        int unlockedCount = game.getUnlockedAchievementsCount();
        String countText = unlockedCount + "/" + achievements.length + " Unlocked";
        GlyphLayout countLayout = new GlyphLayout(smallFont, countText);
        smallFont.setColor(1f, 1f, 1f, 1f);
        smallFont.draw(batch, countText,
            Gdx.graphics.getWidth() / 2f - countLayout.width / 2f,
            Gdx.graphics.getHeight() - 80);

        // Draw achievement text
        for (int i = 0; i < achievements.length; i++) {
            Achievement achievement = achievements[i];
            boolean unlocked = game.isAchievementUnlocked(achievement);
            Rectangle box = achievementBoxes[i];

            if (unlocked) {
                menuFont.setColor(0f, 0f, 0f, 1f);
            } else {
                menuFont.setColor(0.6f, 0.6f, 0.6f, 1f);
            }

            // Achievement name
            menuFont.draw(batch, achievement.getName(), box.x + 15, box.y + box.height - 15);

            // Achievement description
            smallFont.setColor(unlocked ? 0.2f : 0.5f, unlocked ? 0.2f : 0.5f, unlocked ? 0.2f : 0.5f, 1f);
            smallFont.draw(batch, achievement.getDescription(), box.x + 15, box.y + box.height - 40);

            // Checkmark or lock icon (using text)
            menuFont.setColor(unlocked ? 0f : 0.4f, unlocked ? 0f : 0f, unlocked ? 0f : 0f, 1f);
            String icon = unlocked ? "✓" : "🔒";
            menuFont.draw(batch, icon, box.x + box.width - 40, box.y + box.height - 20);
        }

        // Button text
        menuFont.setColor(1, 1, 1, 1);
        menuFont.draw(batch, "BACK",
            backButton.x + (backButton.width - backLayout.width) / 2,
            backButton.y + (backButton.height + backLayout.height) / 2);

        if (!showResetConfirmation) {
            menuFont.draw(batch, "RESET ALL",
                resetButton.x + (resetButton.width - resetLayout.width) / 2,
                resetButton.y + (resetButton.height + resetLayout.height) / 2);
        }

        batch.end();

        // Confirmation dialog
        if (showResetConfirmation) {
            confirmationTimer += delta;
            if (confirmationTimer >= CONFIRMATION_TIME) {
                showResetConfirmation = false;
                confirmationTimer = 0;
            }

            // Semi-transparent overlay
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.7f);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();

            // Confirmation box
            float confirmBoxWidth = 500;
            float confirmBoxHeight = 200;
            float confirmBoxX = Gdx.graphics.getWidth() / 2f - confirmBoxWidth / 2;
            float confirmBoxY = Gdx.graphics.getHeight() / 2f - confirmBoxHeight / 2;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
            shapeRenderer.rect(confirmBoxX, confirmBoxY, confirmBoxWidth, confirmBoxHeight);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1f, 0.3f, 0.3f, 1f);
            Gdx.gl.glLineWidth(3);
            shapeRenderer.rect(confirmBoxX, confirmBoxY, confirmBoxWidth, confirmBoxHeight);
            Gdx.gl.glLineWidth(1);
            shapeRenderer.end();

            boolean yesHover = confirmYesButton.contains(touchX, touchY);
            boolean noHover = confirmNoButton.contains(touchX, touchY);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // Yes button (red)
            shapeRenderer.setColor(yesHover ? 0.8f : 0.6f, 0.2f, 0.2f, 1f);
            shapeRenderer.rect(confirmYesButton.x, confirmYesButton.y, confirmYesButton.width, confirmYesButton.height);
            // No button (green)
            shapeRenderer.setColor(0.2f, yesHover ? 0.6f : 0.8f, 0.2f, 1f);
            shapeRenderer.rect(confirmNoButton.x, confirmNoButton.y, confirmNoButton.width, confirmNoButton.height);
            shapeRenderer.end();

            batch.begin();

            menuFont.setColor(1, 1, 1, 1);
            GlyphLayout warningLayout = new GlyphLayout(menuFont, "Reset all achievements?");
            menuFont.draw(batch, "Reset all achievements?",
                confirmBoxX + (confirmBoxWidth - warningLayout.width) / 2,
                confirmBoxY + confirmBoxHeight - 40);

            smallFont.setColor(0.8f, 0.8f, 0.8f, 1f);
            GlyphLayout subTextLayout = new GlyphLayout(smallFont, "This cannot be undone!");
            smallFont.draw(batch, "This cannot be undone!",
                confirmBoxX + (confirmBoxWidth - subTextLayout.width) / 2,
                confirmBoxY + confirmBoxHeight - 80);

            menuFont.setColor(1, 1, 1, 1);
            GlyphLayout yesLayout = new GlyphLayout(menuFont, "YES");
            GlyphLayout noLayout = new GlyphLayout(menuFont, "NO");

            menuFont.draw(batch, "YES",
                confirmYesButton.x + (confirmYesButton.width - yesLayout.width) / 2,
                confirmYesButton.y + (confirmYesButton.height + yesLayout.height) / 2);

            menuFont.draw(batch, "NO",
                confirmNoButton.x + (confirmNoButton.width - noLayout.width) / 2,
                confirmNoButton.y + (confirmNoButton.height + noLayout.height) / 2);

            batch.end();

            // Handle confirmation input
            if (Gdx.input.justTouched()) {
                if (yesHover) {
                    game.resetAllAchievements();
                    showResetConfirmation = false;
                    confirmationTimer = 0;
                } else if (noHover) {
                    showResetConfirmation = false;
                    confirmationTimer = 0;
                }
            }
        }

        // Input handling
        if (Gdx.input.justTouched() && !showResetConfirmation) {
            if (backHover) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            } else if (resetHover) {
                showResetConfirmation = true;
                confirmationTimer = 0;
            }
        }
    }

    private void drawButton(Rectangle button, boolean hover) {
        if (hover) {
            shapeRenderer.setColor(0.3f, 0.5f, 1f, 0.9f);
        } else {
            shapeRenderer.setColor(0.2f, 0.4f, 0.8f, 0.7f);
        }
        shapeRenderer.rect(button.x, button.y, button.width, button.height);
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;

        float centerX = width / 2f;
        backButton.setPosition(20, 20);
        resetButton.setPosition(width - BUTTON_WIDTH - 20, 20);

        float boxX = centerX - ACHIEVEMENT_BOX_WIDTH / 2;
        float currentY = height - START_Y;

        for (int i = 0; i < achievementBoxes.length; i++) {
            achievementBoxes[i].setPosition(boxX, currentY);
            currentY -= (ACHIEVEMENT_BOX_HEIGHT + ACHIEVEMENT_SPACING);
        }

        confirmYesButton.setPosition(centerX - 220, height / 2 - 80);
        confirmNoButton.setPosition(centerX + 20, height / 2 - 80);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (background != null) background.dispose();
        if (titleFont != null) titleFont.dispose();
        if (menuFont != null) menuFont.dispose();
        if (smallFont != null) smallFont.dispose();
    }
}
