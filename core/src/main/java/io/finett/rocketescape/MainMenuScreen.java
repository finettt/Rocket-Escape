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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class MainMenuScreen implements Screen {
    private final Main game;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture background;
    private Texture rocket;
    private BitmapFont titleFont;
    private BitmapFont menuFont;
    private BitmapFont smallFont;

    private GlyphLayout titleLayout;
    private GlyphLayout playLayout;
    private GlyphLayout settingsLayout;
    private GlyphLayout highScoreLayout;
    private GlyphLayout exitLayout;

    private Rectangle playButton;
    private Rectangle settingsButton;
    private Rectangle exitButton;
    private Rectangle achievementsButton;

    private float rocketY;
    private float rocketAnimTimer;
    private float[] starX;
    private float[] starY;
    private float[] starSpeed;
    private static final int STAR_COUNT = 50;

    private static final float BUTTON_WIDTH = 250f;
    private static final float BUTTON_HEIGHT = 60f;
    private static final float BUTTON_SPACING = 20f;

    private int highScore;

    public MainMenuScreen(Main game) {
        this.game = game;
        this.highScore = game.getHighScore();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        background = new Texture("space-bg.png");
        rocket = new Texture("rocket.png");

        // Safe font generation with try-finally
        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal("PressStart2P-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = 48;
            titleFont = generator.generateFont(parameter);

            parameter.size = 20;
            menuFont = generator.generateFont(parameter);

            parameter.size = 14;
            smallFont = generator.generateFont(parameter);
        } finally {
            if (generator != null) {
                generator.dispose();
            }
        }

        titleLayout = new GlyphLayout();
        playLayout = new GlyphLayout();
        settingsLayout = new GlyphLayout();
        highScoreLayout = new GlyphLayout();
        exitLayout = new GlyphLayout();
        GlyphLayout achievementsLayout = new GlyphLayout();
        titleLayout.setText(titleFont, "ROCKET ESCAPE");
        playLayout.setText(menuFont, "PLAY");
        settingsLayout.setText(menuFont, "SETTINGS");
        exitLayout.setText(menuFont, "EXIT");

        achievementsLayout.setText(menuFont, "ACHIEVEMENTS");

        float centerX = Gdx.graphics.getWidth() / 2f;
        float startY = Gdx.graphics.getHeight() / 2f;

        playButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
        achievementsButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton = new Rectangle(centerX - BUTTON_WIDTH / 2, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 3, BUTTON_WIDTH, BUTTON_HEIGHT);
        starX = new float[STAR_COUNT];
        starY = new float[STAR_COUNT];
        starSpeed = new float[STAR_COUNT];

        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] = MathUtils.random(0, Gdx.graphics.getWidth());
            starY[i] = MathUtils.random(0, Gdx.graphics.getHeight());
            starSpeed[i] = MathUtils.random(20f, 100f);
        }

        rocketY = Gdx.graphics.getHeight() * 0.7f;
        rocketAnimTimer = 0;
    }

    @Override
    public void render(float delta) {
        rocketAnimTimer += delta;

        float rocketOffsetY = MathUtils.sin(rocketAnimTimer * 2f) * 20f;
        float rocketOffsetX = MathUtils.sin(rocketAnimTimer * 1.5f) * 10f;

        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] -= starSpeed[i] * delta;
            if (starX[i] < 0) {
                starX[i] = Gdx.graphics.getWidth();
                starY[i] = MathUtils.random(0, Gdx.graphics.getHeight());
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        boolean playHover = playButton.contains(touchX, touchY);
        boolean achievementsHover = achievementsButton.contains(touchX, touchY);
        boolean settingsHover = settingsButton.contains(touchX, touchY);
        boolean exitHover = exitButton.contains(touchX, touchY);

        batch.begin();
        batch.setColor(0.3f, 0.3f, 0.3f, 1f);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1, 1, 1, 1);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 0.8f);
        for (int i = 0; i < STAR_COUNT; i++) {
            float size = starSpeed[i] / 50f;
            shapeRenderer.circle(starX[i], starY[i], size);
        }
        shapeRenderer.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawButton(playButton, playHover);
        drawButton(achievementsButton, achievementsHover);
        drawButton(settingsButton, settingsHover);
        drawButton(exitButton, exitHover);
        shapeRenderer.end();

        batch.begin();

        float rocketWidth = 80;
        float rocketHeight = 80 * ((float) rocket.getHeight() / rocket.getWidth());
        float rocketX = Gdx.graphics.getWidth() * 0.8f + rocketOffsetX;
        batch.draw(rocket, rocketX, rocketY + rocketOffsetY, rocketWidth, rocketHeight);

        titleFont.setColor(1f, 0.8f, 0f, 1f);
        float titleX = Gdx.graphics.getWidth() / 2f - titleLayout.width / 2f;
        float titleY = Gdx.graphics.getHeight() - 80;
        titleFont.draw(batch, "ROCKET ESCAPE", titleX, titleY);

        menuFont.setColor(1, 1, 1, 1);

        float playTextX = playButton.x + (playButton.width - playLayout.width) / 2;
        float playTextY = playButton.y + (playButton.height + playLayout.height) / 2;
        menuFont.draw(batch, "PLAY", playTextX, playTextY);
        GlyphLayout achievementsLayout = new GlyphLayout(menuFont, "ACHIEVEMENTS");
        float achievementsTextX = achievementsButton.x + (achievementsButton.width - achievementsLayout.width) / 2;
        float achievementsTextY = achievementsButton.y + (achievementsButton.height + achievementsLayout.height) / 2;
        menuFont.draw(batch, "ACHIEVEMENTS", achievementsTextX, achievementsTextY);

        float settingsTextX = settingsButton.x + (settingsButton.width - settingsLayout.width) / 2;
        float settingsTextY = settingsButton.y + (settingsButton.height + settingsLayout.height) / 2;
        menuFont.draw(batch, "SETTINGS", settingsTextX, settingsTextY);

        float exitTextX = exitButton.x + (exitButton.width - exitLayout.width) / 2;
        float exitTextY = exitButton.y + (exitButton.height + exitLayout.height) / 2;
        menuFont.draw(batch, "EXIT", exitTextX, exitTextY);

        smallFont.setColor(0.8f, 0.8f, 0.8f, 1f);
        String highScoreText = "HIGH SCORE: " + highScore;
        highScoreLayout.setText(smallFont, highScoreText);
        smallFont.draw(batch, highScoreText,
            Gdx.graphics.getWidth() / 2f - highScoreLayout.width / 2f,
            playButton.y + playButton.height + 50);

        smallFont.setColor(0.5f, 0.5f, 0.5f, 1f);
        smallFont.draw(batch, "v1.0", 10, 25);

        batch.end();

        if (Gdx.input.justTouched()) {
            if (playHover) {
                game.setScreen(new FirstScreen(game));
                dispose();
            } else if (achievementsHover) {
                game.setScreen(new AchievementsScreen(game));
                dispose();
            } else if (settingsHover) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            } else if (exitHover) {
                Gdx.app.exit();
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
        float startY = height / 2f;

        playButton.setPosition(centerX - BUTTON_WIDTH / 2, startY);
        achievementsButton.setPosition(centerX - BUTTON_WIDTH / 2, startY - BUTTON_HEIGHT - BUTTON_SPACING);
        settingsButton.setPosition(centerX - BUTTON_WIDTH / 2, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 2);
        exitButton.setPosition(centerX - BUTTON_WIDTH / 2, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 3);
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
        if (rocket != null) rocket.dispose();
        if (titleFont != null) titleFont.dispose();
        if (menuFont != null) menuFont.dispose();
        if (smallFont != null) smallFont.dispose();
    }
}
