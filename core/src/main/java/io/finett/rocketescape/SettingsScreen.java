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

public class SettingsScreen implements Screen {
    private final Main game;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture background;
    private BitmapFont titleFont;
    private BitmapFont menuFont;

    private GlyphLayout titleLayout;
    private GlyphLayout soundLayout;
    private GlyphLayout musicLayout;
    private GlyphLayout backLayout;

    private Rectangle soundButton;
    private Rectangle musicButton;
    private Rectangle backButton;

    private boolean soundEnabled;
    private boolean musicEnabled;

    private static final float BUTTON_WIDTH = 300f;
    private static final float BUTTON_HEIGHT = 50f;
    private static final float BUTTON_SPACING = 20f;

    public SettingsScreen(Main game) {
        this.game = game;
        this.soundEnabled = game.isSoundEnabled();
        this.musicEnabled = game.isMusicEnabled();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        background = new Texture("space-bg.png");

        // Safe font generation with try-finally
        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal("PressStart2P-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = 36;
            titleFont = generator.generateFont(parameter);

            parameter.size = 16;
            menuFont = generator.generateFont(parameter);
        } finally {
            if (generator != null) {
                generator.dispose();
            }
        }

        titleLayout = new GlyphLayout();
        soundLayout = new GlyphLayout();
        musicLayout = new GlyphLayout();
        backLayout = new GlyphLayout();

        titleLayout.setText(titleFont, "SETTINGS");
        backLayout.setText(menuFont, "BACK");

        float centerX = Gdx.graphics.getWidth() / 2f;
        float startY = Gdx.graphics.getHeight() / 2f + 50;

        soundButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
        musicButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 3, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        boolean soundHover = soundButton.contains(touchX, touchY);
        boolean musicHover = musicButton.contains(touchX, touchY);
        boolean backHover = backButton.contains(touchX, touchY);

        batch.begin();
        batch.setColor(0.2f, 0.2f, 0.2f, 1f);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1, 1, 1, 1);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawToggleButton(soundButton, soundHover, soundEnabled);
        drawToggleButton(musicButton, musicHover, musicEnabled);
        drawButton(backButton, backHover);
        shapeRenderer.end();

        batch.begin();

        titleFont.setColor(1f, 0.8f, 0f, 1f);
        titleFont.draw(batch, "SETTINGS",
            Gdx.graphics.getWidth() / 2f - titleLayout.width / 2f,
            Gdx.graphics.getHeight() - 80);

        menuFont.setColor(1, 1, 1, 1);

        String soundText = "SOUND: " + (soundEnabled ? "ON" : "OFF");
        soundLayout.setText(menuFont, soundText);
        menuFont.draw(batch, soundText,
            soundButton.x + (soundButton.width - soundLayout.width) / 2,
            soundButton.y + (soundButton.height + soundLayout.height) / 2);

        String musicText = "MUSIC: " + (musicEnabled ? "ON" : "OFF");
        musicLayout.setText(menuFont, musicText);
        menuFont.draw(batch, musicText,
            musicButton.x + (musicButton.width - musicLayout.width) / 2,
            musicButton.y + (musicButton.height + musicLayout.height) / 2);

        menuFont.draw(batch, "BACK",
            backButton.x + (backButton.width - backLayout.width) / 2,
            backButton.y + (backButton.height + backLayout.height) / 2);

        batch.end();

        if (Gdx.input.justTouched()) {
            if (soundHover) {
                soundEnabled = !soundEnabled;
                game.setSoundEnabled(soundEnabled);
            } else if (musicHover) {
                musicEnabled = !musicEnabled;
                game.setMusicEnabled(musicEnabled);
            } else if (backHover) {
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

    private void drawToggleButton(Rectangle button, boolean hover, boolean enabled) {
        if (enabled) {
            if (hover) {
                shapeRenderer.setColor(0.2f, 0.8f, 0.3f, 0.9f);
            } else {
                shapeRenderer.setColor(0.1f, 0.6f, 0.2f, 0.7f);
            }
        } else {
            if (hover) {
                shapeRenderer.setColor(0.8f, 0.3f, 0.2f, 0.9f);
            } else {
                shapeRenderer.setColor(0.6f, 0.2f, 0.1f, 0.7f);
            }
        }
        shapeRenderer.rect(button.x, button.y, button.width, button.height);
    }

    @Override
    public void resize(int width, int height) {
        float centerX = width / 2f;
        float startY = height / 2f + 50;

        soundButton.setPosition(centerX - BUTTON_WIDTH / 2, startY);
        musicButton.setPosition(centerX - BUTTON_WIDTH / 2, startY - BUTTON_HEIGHT - BUTTON_SPACING);
        backButton.setPosition(centerX - BUTTON_WIDTH / 2, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 3);
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
    }
}
