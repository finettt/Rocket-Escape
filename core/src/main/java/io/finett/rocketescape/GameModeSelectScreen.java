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

public class GameModeSelectScreen implements Screen {
    private final Main game;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture background;
    private BitmapFont titleFont;
    private BitmapFont menuFont;
    private BitmapFont smallFont;

    private GlyphLayout titleLayout;
    private Rectangle[] modeButtons;
    private Rectangle backButton;

    // Removed static BUTTON_WIDTH, calculated dynamically
    private static final float BUTTON_HEIGHT = 80f;
    private static final float BUTTON_SPACING = 15f;

    public GameModeSelectScreen(Main game) {
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

            parameter.size = 32;
            titleFont = generator.generateFont(parameter);

            parameter.size = 16;
            menuFont = generator.generateFont(parameter);

            parameter.size = 12;
            smallFont = generator.generateFont(parameter);
        } finally {
            if (generator != null) {
                generator.dispose();
            }
        }

        titleLayout = new GlyphLayout();
        titleLayout.setText(titleFont, "SELECT MODE");

        // --- Calculate Adaptive Uniform Width ---
        float maxTextWidth = 0f;
        GlyphLayout tempLayout = new GlyphLayout();

        // Iterate through all modes to find the widest text
        for (GameMode mode : GameMode.values()) {
            // Check Title width
            tempLayout.setText(menuFont, mode.getDisplayName());
            if (tempLayout.width > maxTextWidth) {
                maxTextWidth = tempLayout.width;
            }
            // Check Description width
            tempLayout.setText(smallFont, mode.getDescription());
            if (tempLayout.width > maxTextWidth) {
                maxTextWidth = tempLayout.width;
            }
        }

        // Add padding (60px total) and set minimum width (300px)
        float buttonWidth = Math.max(300f, maxTextWidth + 60f);

        // Ensure it doesn't exceed screen width
        buttonWidth = Math.min(buttonWidth, Gdx.graphics.getWidth() - 40f);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float startY = Gdx.graphics.getHeight() - 180;

        modeButtons = new Rectangle[GameMode.values().length];
        for (int i = 0; i < GameMode.values().length; i++) {
            modeButtons[i] = new Rectangle(
                centerX - buttonWidth / 2,
                startY - i * (BUTTON_HEIGHT + BUTTON_SPACING),
                buttonWidth,
                BUTTON_HEIGHT
            );
        }

        backButton = new Rectangle(
            centerX - buttonWidth / 2, // Match mode buttons width or keep smaller
            40,
            buttonWidth, // Using same width for consistency
            50
        );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        batch.begin();
        batch.setColor(0.3f, 0.3f, 0.3f, 1f);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1, 1, 1, 1);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Mode buttons
        for (int i = 0; i < modeButtons.length; i++) {
            boolean hover = modeButtons[i].contains(touchX, touchY);
            if (hover) {
                shapeRenderer.setColor(0.3f, 0.5f, 1f, 0.9f);
            } else {
                shapeRenderer.setColor(0.2f, 0.4f, 0.8f, 0.7f);
            }
            shapeRenderer.rect(modeButtons[i].x, modeButtons[i].y, modeButtons[i].width, modeButtons[i].height);
        }

        // Back button
        boolean backHover = backButton.contains(touchX, touchY);
        shapeRenderer.setColor(backHover ? 0.6f : 0.4f, 0.4f, 0.4f, 0.8f);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);

        shapeRenderer.end();

        batch.begin();

        titleFont.setColor(1f, 0.8f, 0f, 1f);
        titleFont.draw(batch, "SELECT MODE",
            Gdx.graphics.getWidth() / 2f - titleLayout.width / 2f,
            Gdx.graphics.getHeight() - 60);

        menuFont.setColor(1, 1, 1, 1);
        smallFont.setColor(0.8f, 0.8f, 0.8f, 1f);

        GameMode[] modes = GameMode.values();
        for (int i = 0; i < modes.length; i++) {
            Rectangle btn = modeButtons[i];
            GameMode mode = modes[i];

            // Mode Name
            GlyphLayout nameLayout = new GlyphLayout(menuFont, mode.getDisplayName());
            menuFont.draw(batch, mode.getDisplayName(),
                btn.x + (btn.width - nameLayout.width) / 2,
                btn.y + btn.height - 20);

            // Mode Description
            GlyphLayout descLayout = new GlyphLayout(smallFont, mode.getDescription());
            smallFont.draw(batch, mode.getDescription(),
                btn.x + (btn.width - descLayout.width) / 2,
                btn.y + 30);
        }

        menuFont.setColor(1, 1, 1, 1);
        GlyphLayout backLayout = new GlyphLayout(menuFont, "BACK");
        menuFont.draw(batch, "BACK",
            backButton.x + (backButton.width - backLayout.width) / 2,
            backButton.y + (backButton.height + backLayout.height) / 2);

        batch.end();

        if (Gdx.input.justTouched()) {
            for (int i = 0; i < modeButtons.length; i++) {
                if (modeButtons[i].contains(touchX, touchY)) {
                    // Start game with selected mode
                    game.setScreen(new FirstScreen(game, GameMode.values()[i]));
                    dispose();
                    return;
                }
            }

            if (backButton.contains(touchX, touchY)) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

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
