package io.finett.rocketescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PauseMenuScreen implements Screen {
    private final Main game;
    private final FirstScreen gameScreen;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont titleFont;
    private BitmapFont menuFont;

    private GlyphLayout pausedLayout;
    private GlyphLayout resumeLayout;
    private GlyphLayout restartLayout;
    private GlyphLayout mainMenuLayout;

    private Rectangle resumeButton;
    private Rectangle restartButton;
    private Rectangle mainMenuButton;

    private static final float BUTTON_WIDTH = 250f;
    private static final float BUTTON_HEIGHT = 50f;
    private static final float BUTTON_SPACING = 15f;

    public PauseMenuScreen(Main game, FirstScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal("PressStart2P-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = 36;
            titleFont = generator.generateFont(parameter);

            parameter.size = 18;
            menuFont = generator.generateFont(parameter);
        } finally {
            if (generator != null) {
                generator.dispose();
            }
        }

        pausedLayout = new GlyphLayout();
        resumeLayout = new GlyphLayout();
        restartLayout = new GlyphLayout();
        mainMenuLayout = new GlyphLayout();

        pausedLayout.setText(titleFont, "PAUSED");
        resumeLayout.setText(menuFont, "RESUME");
        restartLayout.setText(menuFont, "RESTART");
        mainMenuLayout.setText(menuFont, "MAIN MENU");

        float centerX = Gdx.graphics.getWidth() / 2f;
        float startY = Gdx.graphics.getHeight() / 2f + 50;

        resumeButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
        restartButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        mainMenuButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 2, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void render(float delta) {
        // Полупрозрачный фон
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        boolean resumeHover = resumeButton.contains(touchX, touchY);
        boolean restartHover = restartButton.contains(touchX, touchY);
        boolean mainMenuHover = mainMenuButton.contains(touchX, touchY);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawButton(resumeButton, resumeHover);
        drawButton(restartButton, restartHover);
        drawButton(mainMenuButton, mainMenuHover);
        shapeRenderer.end();

        batch.begin();

        titleFont.setColor(1f, 0.8f, 0f, 1f);
        titleFont.draw(batch, "PAUSED",
            Gdx.graphics.getWidth() / 2f - pausedLayout.width / 2f,
            Gdx.graphics.getHeight() - 100);

        menuFont.setColor(1, 1, 1, 1);

        menuFont.draw(batch, "RESUME",
            resumeButton.x + (resumeButton.width - resumeLayout.width) / 2,
            resumeButton.y + (resumeButton.height + resumeLayout.height) / 2);

        menuFont.draw(batch, "RESTART",
            restartButton.x + (restartButton.width - restartLayout.width) / 2,
            restartButton.y + (restartButton.height + restartLayout.height) / 2);

        menuFont.draw(batch, "MAIN MENU",
            mainMenuButton.x + (mainMenuButton.width - mainMenuLayout.width) / 2,
            mainMenuButton.y + (mainMenuButton.height + mainMenuLayout.height) / 2);

        batch.end();

        if (Gdx.input.justTouched()) {
            if (resumeHover) {
                game.setScreen(gameScreen);
                dispose();
            } else if (restartHover) {
                gameScreen.dispose();
                game.setScreen(new FirstScreen(game));
                dispose();
            } else if (mainMenuHover) {
                gameScreen.dispose();
                game.setScreen(new MainMenuScreen(game));
                dispose();
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
        float startY = height / 2f + 50;

        resumeButton.setPosition(centerX - BUTTON_WIDTH / 2, startY);
        restartButton.setPosition(centerX - BUTTON_WIDTH / 2, startY - BUTTON_HEIGHT - BUTTON_SPACING);
        mainMenuButton.setPosition(centerX - BUTTON_WIDTH / 2, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 2);
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
        if (titleFont != null) titleFont.dispose();
        if (menuFont != null) menuFont.dispose();
    }
}
