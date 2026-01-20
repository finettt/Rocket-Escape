package io.finett.rocketescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class FirstScreen implements Screen {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture background;
    private Texture rocket;
    private Texture[] spikes;
    private BitmapFont font;
    private BitmapFont comboFont;

    private Rectangle rocketRect;
    private Array<SpikeData> spikeData;
    private float rocketY;
    private float rocketVelocity;
    private float gravity;
    private float spikeTimer;
    private float nextSpikeDelay;
    private int score;
    private boolean gameOver;
    private boolean ready;
    private boolean go;
    private float readyTimer;
    private float goTimer;

    private ParticleEffect particleEffect;
    private ParticleEffectPool particleEffectPool;
    private Pool<ParticleEffectPool.PooledEffect> particleEffects;

    private Array<ScorePopup> scorePopups;
    private float shakeTimer;
    private float shakeIntensity;

    // Lives system
    private int lives;
    private static final int MAX_LIVES = 3;
    private float invulnerabilityTimer;
    private static final float INVULNERABILITY_TIME = 2.0f;

    // Difficulty progression
    private float difficultyMultiplier;
    private static final float DIFFICULTY_INCREASE_RATE = 0.01f;
    private static final float MAX_DIFFICULTY = 2.5f;
    private static final float BASE_SPIKE_SPEED = 200f;

    // Combo system constants
    private int combo;
    private int maxCombo;
    private float comboTimer;
    private boolean comboExpiring;
    private static final float COMBO_TIMEOUT = 5f;
    private static final float COMBO_EXPIRING_THRESHOLD = 1.5f;
    private static final int COMBO_THRESHOLD = 3;
    private static final int COMBO_BONUS_MULTIPLIER = 2;
    private static final int MAX_COMBO_VALUE = 999;

    private static final float ROCKET_SIZE = 64;
    private float spikeWidth;
    private float spikeGap;
    private static final float SPIKE_SPACING = 300;

    // Obstacle spacing constants
    private static final float MIN_SPIKE_DELAY = 2.0f;
    private static final float MAX_SPIKE_DELAY = 4.5f;

    private static final float BACKGROUND_BRIGHTNESS = 0.5f;
    private static final float DEFAULT_BRIGHTNESS = 1f;

    // UI positioning constants
    private static final float UI_MARGIN = 20f;
    private static final float COMBO_BAR_WIDTH = 100f;
    private static final float COMBO_BAR_HEIGHT = 8f;
    private static final float COMBO_BAR_Y_OFFSET = 55f;
    private static final float HEART_SIZE = 24f;
    private static final float HEART_SPACING = 35f;

    // Combo bar colors
    private static final float COMBO_BAR_BG_COLOR = 0.3f;
    private static final float COMBO_BAR_FULL_R = 0f;
    private static final float COMBO_BAR_FULL_G = 1f;
    private static final float COMBO_BAR_EMPTY_R = 1f;
    private static final float COMBO_BAR_EMPTY_G = 0f;

    // Heart colors
    private static final float HEART_FULL_R = 1f;
    private static final float HEART_FULL_G = 0.2f;
    private static final float HEART_FULL_B = 0.2f;
    private static final float HEART_EMPTY_R = 0.3f;
    private static final float HEART_EMPTY_G = 0.3f;
    private static final float HEART_EMPTY_B = 0.3f;

    // Cached GlyphLayouts
    private GlyphLayout comboLayout;
    private GlyphLayout readyLayout;
    private GlyphLayout goLayout;
    private GlyphLayout gameOverLayout;
    private GlyphLayout restartLayout;
    private GlyphLayout difficultyLayout;

    // Colors for score popups
    private static final float COMBO_COLOR_R = 1f;
    private static final float COMBO_COLOR_G = 0.84f;
    private static final float COMBO_COLOR_B = 0f;

    private class SpikeData {
        Rectangle rect;
        int textureIndex;
        boolean isTop;
        Vector2 p1, p2, p3;
        boolean scored;

        public SpikeData(Rectangle rect, int textureIndex, boolean isTop) {
            this.rect = rect;
            this.textureIndex = textureIndex;
            this.isTop = isTop;
            this.p1 = new Vector2();
            this.p2 = new Vector2();
            this.p3 = new Vector2();
            this.scored = false;
            updateTriangle();
        }

        public void updateTriangle() {
            if (isTop) {
                p1.set(rect.x, rect.y + rect.height);
                p2.set(rect.x + rect.width, rect.y + rect.height);
                p3.set(rect.x + rect.width / 2, rect.y);
            } else {
                p1.set(rect.x, rect.y);
                p2.set(rect.x + rect.width, rect.y);
                p3.set(rect.x + rect.width / 2, rect.y + rect.height);
            }
        }
    }

    private class ScorePopup {
        String text;
        float x, y;
        float velocity;
        float alpha;
        float timer;
        boolean isCombo;

        public ScorePopup(String text, float x, float y, boolean isCombo) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.velocity = -50f;
            this.alpha = 1f;
            this.timer = 0f;
            this.isCombo = isCombo;
        }

        public boolean update(float delta) {
            y += velocity * delta;
            timer += delta;
            if (timer > 1f) {
                alpha = Interpolation.fade.apply(1f, 0f, (timer - 1f) / 0.5f);
            }
            return timer > 1.5f;
        }

        public void applyColor(BitmapFont font) {
            if (isCombo) {
                font.setColor(COMBO_COLOR_R, COMBO_COLOR_G, COMBO_COLOR_B, alpha);
            } else {
                font.setColor(1, 1, 1, alpha);
            }
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        background = new Texture("space-bg.png");
        rocket = new Texture("rocket.png");

        spikes = new Texture[5];
        for (int i = 0; i < 5; i++) {
            spikes[i] = new Texture("spike_" + (i + 1) + ".png");
        }

        spikeWidth = Gdx.graphics.getWidth() * 0.08f;
        spikeGap = Gdx.graphics.getHeight() * 0.25f;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        font = generator.generateFont(parameter);

        parameter.size = 32;
        comboFont = generator.generateFont(parameter);
        generator.dispose();

        // Initialize cached GlyphLayouts
        comboLayout = new GlyphLayout();
        readyLayout = new GlyphLayout();
        goLayout = new GlyphLayout();
        gameOverLayout = new GlyphLayout();
        restartLayout = new GlyphLayout();
        difficultyLayout = new GlyphLayout();

        // Pre-compute static text layouts
        readyLayout.setText(font, "READY?");
        goLayout.setText(font, "GO!");
        gameOverLayout.setText(font, "GAME OVER");
        restartLayout.setText(font, "Tap to restart");

        rocketRect = new Rectangle();
        spikeData = new Array<SpikeData>();
        scorePopups = new Array<ScorePopup>();

        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particles/rocket-trail.p"), Gdx.files.internal("particles"));
        particleEffectPool = new ParticleEffectPool(particleEffect, 1, 10);
        particleEffects = new Pool<ParticleEffectPool.PooledEffect>() {
            @Override
            protected ParticleEffectPool.PooledEffect newObject() {
                return particleEffectPool.obtain();
            }
        };

        resetGame();
    }

    private void resetGame() {
        rocketY = Gdx.graphics.getHeight() / 2;
        rocketVelocity = 0;
        gravity = -15f;
        spikeTimer = 0;
        difficultyMultiplier = 1.0f;
        nextSpikeDelay = getRandomSpikeDelay();
        score = 0;
        combo = 0;
        maxCombo = 0;
        comboTimer = 0;
        comboExpiring = false;
        gameOver = false;
        ready = true;
        go = false;
        readyTimer = 0;
        goTimer = 0;
        shakeTimer = 0;
        shakeIntensity = 0;
        lives = MAX_LIVES;
        invulnerabilityTimer = 0;

        float rocketWidth = ROCKET_SIZE;
        float rocketHeight = ROCKET_SIZE * ((float)rocket.getHeight() / rocket.getWidth());
        rocketRect.set(Gdx.graphics.getWidth() / 4, rocketY, rocketWidth, rocketHeight);

        spikeData.clear();
        scorePopups.clear();
    }

    /**
     * Генерирует случайное время задержки между препятствиями с учетом сложности
     */
    private float getRandomSpikeDelay() {
        float adjustedMinDelay = Math.max(1.0f, MIN_SPIKE_DELAY / difficultyMultiplier);
        float adjustedMaxDelay = Math.max(1.5f, MAX_SPIKE_DELAY / difficultyMultiplier);
        return MathUtils.random(adjustedMinDelay, adjustedMaxDelay);
    }

    /**
     * Обновляет множитель сложности на основе текущего счета
     */
    private void updateDifficulty() {
        difficultyMultiplier = Math.min(MAX_DIFFICULTY, 1.0f + (score * DIFFICULTY_INCREASE_RATE));
    }

    /**
     * Возвращает текущую скорость препятствий с учетом сложности
     */
    private float getCurrentSpikeSpeed() {
        return BASE_SPIKE_SPEED * difficultyMultiplier;
    }

    /**
     * Рисует сердце с помощью ShapeRenderer
     */
    private void drawHeart(float x, float y, float size, boolean filled) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (filled) {
            shapeRenderer.setColor(HEART_FULL_R, HEART_FULL_G, HEART_FULL_B, 1f);
        } else {
            shapeRenderer.setColor(HEART_EMPTY_R, HEART_EMPTY_G, HEART_EMPTY_B, 1f);
        }

        // Рисуем сердце из двух кругов и треугольника
        float halfSize = size / 2;
        float quarterSize = size / 4;

        // Левый круг
        shapeRenderer.circle(x + quarterSize, y + halfSize, quarterSize, 20);
        // Правый круг
        shapeRenderer.circle(x + halfSize + quarterSize, y + halfSize, quarterSize, 20);
        // Нижний треугольник
        shapeRenderer.triangle(
            x, y + halfSize,
            x + size, y + halfSize,
            x + halfSize, y
        );

        shapeRenderer.end();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (ready) {
            readyTimer += delta;
            if (readyTimer > 1f) {
                ready = false;
                go = true;
            }
        } else if (go) {
            goTimer += delta;
            if (goTimer > 1f) {
                go = false;
            }
        } else if (!gameOver) {
            updateGame(delta);
        }

        // Update invulnerability timer
        if (invulnerabilityTimer > 0) {
            invulnerabilityTimer -= delta;
        }

        for (int i = 0; i < scorePopups.size; i++) {
            ScorePopup popup = scorePopups.get(i);
            if (popup.update(delta)) {
                scorePopups.removeIndex(i);
                i--;
            }
        }

        if (shakeTimer > 0) {
            shakeTimer -= delta;
            if (shakeTimer <= 0) {
                shakeIntensity = 0;
            }
        }

        batch.begin();

        float shakeX = 0;
        if (shakeIntensity > 0) {
            shakeX = MathUtils.random(-shakeIntensity, shakeIntensity);
        }

        batch.setColor(BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, BACKGROUND_BRIGHTNESS, DEFAULT_BRIGHTNESS);
        batch.draw(background, shakeX, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(DEFAULT_BRIGHTNESS, DEFAULT_BRIGHTNESS, DEFAULT_BRIGHTNESS, DEFAULT_BRIGHTNESS);

        for (SpikeData spike : spikeData) {
            if (spike.isTop) {
                batch.draw(spikes[spike.textureIndex], spike.rect.x + shakeX, spike.rect.y, spike.rect.width, spike.rect.height);
            } else {
                batch.draw(spikes[spike.textureIndex],
                    spike.rect.x + shakeX,
                    spike.rect.y,
                    spike.rect.width / 2,
                    spike.rect.height / 2,
                    spike.rect.width,
                    spike.rect.height,
                    1,
                    1,
                    180,
                    0,
                    0,
                    spikes[spike.textureIndex].getWidth(),
                    spikes[spike.textureIndex].getHeight(),
                    false,
                    false
                );
            }
        }

        // Draw rocket with invulnerability flashing effect
        if (invulnerabilityTimer > 0) {
            // Flash rocket when invulnerable
            float flashAlpha = (float)Math.sin(invulnerabilityTimer * 20) * 0.5f + 0.5f;
            batch.setColor(1, 1, 1, flashAlpha);
        }
        batch.draw(rocket, rocketRect.x + shakeX, rocketRect.y, rocketRect.width, rocketRect.height);
        batch.setColor(1, 1, 1, 1);

        for (ScorePopup popup : scorePopups) {
            popup.applyColor(font);
            font.draw(batch, popup.text, popup.x + shakeX, popup.y);
        }
        font.setColor(1, 1, 1, 1);

        // Draw score
        font.draw(batch, "Score: " + score, UI_MARGIN + shakeX, Gdx.graphics.getHeight() - UI_MARGIN);

        // Draw difficulty indicator
        String difficultyText = String.format("x%.1f", difficultyMultiplier);
        difficultyLayout.setText(font, difficultyText);
        font.draw(batch, difficultyText, UI_MARGIN + shakeX, Gdx.graphics.getHeight() - UI_MARGIN - 35);

        // Draw combo
        if (combo >= 2 && !gameOver && comboFont != null) {
            float comboIntensity = Math.min(1f, (float)combo / 10f);

            if (comboExpiring) {
                float flash = (float)Math.sin(comboTimer * 10) * 0.5f + 0.5f;
                comboFont.setColor(1f, flash, flash, 1f);
            } else {
                comboFont.setColor(1f, 1f - (comboIntensity * 0.16f), 1f - comboIntensity, 1f);
            }

            String comboText = "COMBO x" + combo;
            comboLayout.setText(comboFont, comboText);
            comboFont.draw(batch, comboText, Gdx.graphics.getWidth() - comboLayout.width - UI_MARGIN + shakeX, Gdx.graphics.getHeight() - UI_MARGIN);

            comboFont.setColor(1, 1, 1, 1);
        }

        batch.end();

        // Draw hearts (lives) using ShapeRenderer - только 3 сердца
        float heartStartX = Gdx.graphics.getWidth() / 2 - (MAX_LIVES * HEART_SPACING) / 2;
        float heartY = Gdx.graphics.getHeight() - UI_MARGIN - HEART_SIZE - 10;

        for (int i = 0; i < MAX_LIVES; i++) {
            boolean filled = (i < lives);
            drawHeart(heartStartX + i * HEART_SPACING + shakeX, heartY, HEART_SIZE, filled);
        }

        // Draw combo timer bar
        if (combo >= 2 && !gameOver && shapeRenderer != null) {
            float barX = Gdx.graphics.getWidth() - COMBO_BAR_WIDTH - UI_MARGIN + shakeX;
            float barY = Gdx.graphics.getHeight() - COMBO_BAR_Y_OFFSET;
            float fillWidth = (comboTimer / COMBO_TIMEOUT) * COMBO_BAR_WIDTH;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(COMBO_BAR_BG_COLOR, COMBO_BAR_BG_COLOR, COMBO_BAR_BG_COLOR, 1f);
            shapeRenderer.rect(barX, barY, COMBO_BAR_WIDTH, COMBO_BAR_HEIGHT);

            float timerRatio = comboTimer / COMBO_TIMEOUT;
            float r = COMBO_BAR_EMPTY_R + (COMBO_BAR_FULL_R - COMBO_BAR_EMPTY_R) * timerRatio;
            float g = COMBO_BAR_EMPTY_G + (COMBO_BAR_FULL_G - COMBO_BAR_EMPTY_G) * timerRatio;
            shapeRenderer.setColor(r, g, 0f, 1f);
            shapeRenderer.rect(barX, barY, fillWidth, COMBO_BAR_HEIGHT);

            shapeRenderer.end();
        }

        batch.begin();

        if (ready) {
            font.draw(batch, "READY?", Gdx.graphics.getWidth() / 2 - readyLayout.width / 2 + shakeX, Gdx.graphics.getHeight() / 2);
        } else if (go) {
            font.draw(batch, "GO!", Gdx.graphics.getWidth() / 2 - goLayout.width / 2 + shakeX, Gdx.graphics.getHeight() / 2);
        } else if (gameOver) {
            font.draw(batch, "GAME OVER", Gdx.graphics.getWidth() / 2 - gameOverLayout.width / 2 + shakeX, Gdx.graphics.getHeight() / 2);
            font.draw(batch, "Tap to restart", Gdx.graphics.getWidth() / 2 - restartLayout.width / 2 + shakeX, Gdx.graphics.getHeight() / 2 - 40);
        }

        batch.end();

        if (Gdx.input.justTouched()) {
            if (gameOver) {
                resetGame();
            } else if (!ready && !go) {
                rocketVelocity = 500f;

                ParticleEffectPool.PooledEffect effect = particleEffects.obtain();
                effect.setPosition(rocketRect.x + rocketRect.width / 2, rocketRect.y + rocketRect.height / 2);
                effect.start();
                particleEffectPool.free(effect);
            }
        }
    }

    private void updateGame(float delta) {
        rocketVelocity += gravity;
        rocketY += rocketVelocity * delta;
        rocketRect.y = rocketY;

        if (rocketRect.y < 0) {
            rocketRect.y = 0;
            rocketY = 0;
            rocketVelocity = 0;
        } else if (rocketRect.y > Gdx.graphics.getHeight() - rocketRect.height) {
            rocketRect.y = Gdx.graphics.getHeight() - rocketRect.height;
            rocketY = Gdx.graphics.getHeight() - rocketRect.height;
            rocketVelocity = 0;
        }

        updateComboTimer(delta);
        updateDifficulty();

        spikeTimer += delta;

        // Проверяем превышение времени
        if (spikeTimer >= nextSpikeDelay) {
            spawnSpikes();
            spikeTimer = 0;
            nextSpikeDelay = getRandomSpikeDelay();
        }

        float currentSpeed = getCurrentSpikeSpeed();

        for (int i = 0; i < spikeData.size; i++) {
            SpikeData spike = spikeData.get(i);
            spike.rect.x -= currentSpeed * delta;
            spike.updateTriangle();

            if (!spike.scored && !spike.isTop) {
                float rocketCenterX = rocketRect.x + rocketRect.width / 2;
                float spikeCenterX = spike.rect.x + spike.rect.width / 2;

                if (rocketCenterX > spikeCenterX) {
                    spike.scored = true;
                    incrementCombo();
                    int pointsEarned = calculatePoints();
                    score += pointsEarned;
                }
            }

            if (spike.rect.x + spike.rect.width < 0) {
                spikeData.removeIndex(i);
                i--;
            }
        }

        checkCollisions();
    }

    private void updateComboTimer(float delta) {
        if (combo > 0) {
            comboTimer -= delta;
            comboExpiring = comboTimer <= COMBO_EXPIRING_THRESHOLD && comboTimer > 0;

            if (comboTimer <= 0) {
                combo = 0;
                comboTimer = 0;
                comboExpiring = false;
            }
        }
    }

    private void incrementCombo() {
        if (combo < MAX_COMBO_VALUE) {
            combo++;
        }
        if (combo > maxCombo) {
            maxCombo = combo;
        }
        comboTimer = COMBO_TIMEOUT;
        comboExpiring = false;
    }

    private int calculatePoints() {
        int pointsEarned = 1;
        scorePopups.add(new ScorePopup("+1", rocketRect.x, rocketRect.y + rocketRect.height, false));

        if (combo >= COMBO_THRESHOLD) {
            int bonusPoints = (combo - COMBO_THRESHOLD + 1) * COMBO_BONUS_MULTIPLIER;
            pointsEarned += bonusPoints;
            scorePopups.add(new ScorePopup("+" + bonusPoints + " COMBO!", rocketRect.x, rocketRect.y + rocketRect.height + 30, true));
        }

        return pointsEarned;
    }

    private void spawnSpikes() {
        float height = Gdx.graphics.getHeight();
        float gapStart = MathUtils.random(100, height - spikeGap - 100);

        int spikeIndex = MathUtils.random(0, 4);

        Rectangle topSpike = new Rectangle(Gdx.graphics.getWidth(), gapStart + spikeGap, spikeWidth, height - (gapStart + spikeGap));
        spikeData.add(new SpikeData(topSpike, spikeIndex, true));

        Rectangle bottomSpike = new Rectangle(Gdx.graphics.getWidth(), 0, spikeWidth, gapStart);
        spikeData.add(new SpikeData(bottomSpike, spikeIndex, false));
    }

    private boolean pointInTriangle(float px, float py, Vector2 p1, Vector2 p2, Vector2 p3) {
        float d1 = sign(px, py, p1.x, p1.y, p2.x, p2.y);
        float d2 = sign(px, py, p2.x, p2.y, p3.x, p3.y);
        float d3 = sign(px, py, p3.x, p3.y, p1.x, p1.y);

        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(hasNeg && hasPos);
    }

    private float sign(float px, float py, float x1, float y1, float x2, float y2) {
        return (px - x2) * (y1 - y2) - (x1 - x2) * (py - y2);
    }

    private boolean rectangleIntersectsTriangle(Rectangle rect, Vector2 p1, Vector2 p2, Vector2 p3) {
        if (pointInTriangle(rect.x, rect.y, p1, p2, p3)) return true;
        if (pointInTriangle(rect.x + rect.width, rect.y, p1, p2, p3)) return true;
        if (pointInTriangle(rect.x, rect.y + rect.height, p1, p2, p3)) return true;
        if (pointInTriangle(rect.x + rect.width, rect.y + rect.height, p1, p2, p3)) return true;

        if (pointInTriangle(rect.x + rect.width / 2, rect.y + rect.height / 2, p1, p2, p3)) return true;

        if (pointInTriangle(rect.x + rect.width / 2, rect.y, p1, p2, p3)) return true;
        if (pointInTriangle(rect.x + rect.width / 2, rect.y + rect.height, p1, p2, p3)) return true;
        if (pointInTriangle(rect.x, rect.y + rect.height / 2, p1, p2, p3)) return true;
        if (pointInTriangle(rect.x + rect.width, rect.y + rect.height / 2, p1, p2, p3)) return true;

        return false;
    }

    private void checkCollisions() {
        // Skip collision check if invulnerable
        if (invulnerabilityTimer > 0) {
            return;
        }

        for (SpikeData spike : spikeData) {
            if (rectangleIntersectsTriangle(rocketRect, spike.p1, spike.p2, spike.p3)) {
                handleCollision();
                break;
            }
        }
    }

    private void handleCollision() {
        lives--;

        if (lives <= 0) {
            gameOver = true;
            shakeTimer = 0.5f;
            shakeIntensity = 10f;
        } else {
            // Give invulnerability and visual feedback
            invulnerabilityTimer = INVULNERABILITY_TIME;
            shakeTimer = 0.3f;
            shakeIntensity = 5f;

            // Reset combo on hit
            combo = 0;
            comboTimer = 0;
            comboExpiring = false;

            // Add visual feedback
            scorePopups.add(new ScorePopup("-1 life", rocketRect.x, rocketRect.y + rocketRect.height, false));
        }
    }

    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;

        spikeWidth = width * 0.08f;
        spikeGap = height * 0.25f;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
        if (background != null) {
            background.dispose();
            background = null;
        }
        if (rocket != null) {
            rocket.dispose();
            rocket = null;
        }
        if (spikes != null) {
            for (int i = 0; i < spikes.length; i++) {
                if (spikes[i] != null) {
                    spikes[i].dispose();
                    spikes[i] = null;
                }
            }
            spikes = null;
        }
        if (font != null) {
            font.dispose();
            font = null;
        }
        if (comboFont != null) {
            comboFont.dispose();
            comboFont = null;
        }
        if (particleEffect != null) {
            particleEffect.dispose();
            particleEffect = null;
        }
    }
}
