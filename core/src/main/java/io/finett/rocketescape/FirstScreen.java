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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    private SpriteBatch batch;
    private Texture background;
    private Texture rocket;
    private Texture[] spikes;
    private BitmapFont font;

    private Rectangle rocketRect;
    private Array<SpikeData> spikeData;
    private float rocketY;
    private float rocketVelocity;
    private float gravity;
    private float spikeTimer;
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

    private static final float ROCKET_SIZE = 64;
    private float spikeWidth;
    private float spikeGap;
    private static final float SPIKE_SPACING = 300;

    // Background brightness constants for better contrast with foreground elements
    private static final float BACKGROUND_BRIGHTNESS = 0.5f;
    private static final float DEFAULT_BRIGHTNESS = 1f;

    private class SpikeData {
        Rectangle rect;
        int textureIndex;
        boolean isTop;
        Vector2 p1, p2, p3;

        public SpikeData(Rectangle rect, int textureIndex, boolean isTop) {
            this.rect = rect;
            this.textureIndex = textureIndex;
            this.isTop = isTop;
            this.p1 = new Vector2();
            this.p2 = new Vector2();
            this.p3 = new Vector2();
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

        public ScorePopup(String text, float x, float y) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.velocity = -50f;
            this.alpha = 1f;
            this.timer = 0f;
        }

        public boolean update(float delta) {
            y += velocity * delta;
            timer += delta;
            if (timer > 1f) {
                alpha = Interpolation.fade.apply(1f, 0f, (timer - 1f) / 0.5f);
            }
            return timer > 1.5f;
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
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
        generator.dispose();

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
        score = 0;
        gameOver = false;
        ready = true;
        go = false;
        readyTimer = 0;
        goTimer = 0;
        shakeTimer = 0;
        shakeIntensity = 0;

        float rocketWidth = ROCKET_SIZE;
        float rocketHeight = ROCKET_SIZE * ((float)rocket.getHeight() / rocket.getWidth());
        rocketRect.set(Gdx.graphics.getWidth() / 4, rocketY, rocketWidth, rocketHeight);

        spikeData.clear();
        scorePopups.clear();
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

        // Apply darker tint to background for better contrast with foreground elements
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

        batch.draw(rocket, rocketRect.x + shakeX, rocketRect.y, rocketRect.width, rocketRect.height);

        for (ScorePopup popup : scorePopups) {
            font.setColor(1, 1, 1, popup.alpha);
            font.draw(batch, popup.text, popup.x + shakeX, popup.y);
        }
        font.setColor(1, 1, 1, 1);

        font.draw(batch, "Score: " + score, 20 + shakeX, Gdx.graphics.getHeight() - 20);

        if (ready) {
            GlyphLayout layout = new GlyphLayout(font, "READY?");
            font.draw(batch, "READY?", Gdx.graphics.getWidth() / 2 - layout.width / 2 + shakeX, Gdx.graphics.getHeight() / 2);
        } else if (go) {
            GlyphLayout layout = new GlyphLayout(font, "GO!");
            font.draw(batch, "GO!", Gdx.graphics.getWidth() / 2 - layout.width / 2 + shakeX, Gdx.graphics.getHeight() / 2);
        } else if (gameOver) {
            GlyphLayout gameOverLayout = new GlyphLayout(font, "GAME OVER");
            GlyphLayout restartLayout = new GlyphLayout(font, "Tap to restart");
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

        spikeTimer += delta;
        if (spikeTimer > 3.5f) {
            spawnSpikes();
            spikeTimer = 0;
        }

        boolean scoredThisFrame = false;
        for (int i = 0; i < spikeData.size; i++) {
            SpikeData spike = spikeData.get(i);
            spike.rect.x -= 200 * delta;
            spike.updateTriangle();

            if (spike.rect.x + spike.rect.width < 0) {
                if (!scoredThisFrame && !spike.isTop) {
                    score++;
                    scoredThisFrame = true;
                    scorePopups.add(new ScorePopup("+1", rocketRect.x, rocketRect.y + rocketRect.height));
                }
                spikeData.removeIndex(i);
                i--;
            }
        }

        checkCollisions();
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
        for (SpikeData spike : spikeData) {
            if (rectangleIntersectsTriangle(rocketRect, spike.p1, spike.p2, spike.p3)) {
                gameOver = true;
                shakeTimer = 0.5f;
                shakeIntensity = 10f;
                break;
            }
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
        batch.dispose();
        background.dispose();
        rocket.dispose();
        for (Texture spike : spikes) {
            spike.dispose();
        }
        font.dispose();
        particleEffect.dispose();
    }
}
