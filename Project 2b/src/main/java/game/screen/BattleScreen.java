package game.screen;

import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import game.GameState;
import game.ShadowAliens;
import game.config.GameConfig;
import game.entity.*;
import game.power.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BattleScreen {

    // game state
    private int currentFrame;
    public static int currentTimeScale = 1;
    public int timeScale = 1;
    private boolean isInvincible = false;
    private boolean isPaused = false;
    private int score = 0;

    // wave
    private int currentWave = 1;
    private int totalWaves;
    private List<String> spawnedEnemies = new ArrayList<>();
    private List<String> spawnedPowerUps = new ArrayList<>();

    // entity
    private Player player;
    private final List<Enemy> enemyList = new ArrayList<>();
    private final List<Projectile> projectileList = new ArrayList<>();
    private final List<Explosion> explosionList = new ArrayList<>();
    private final List<LifeIcon> lifeIconList = new ArrayList<>();
    private final List<PowerUp> powerUpList = new ArrayList<>();


    private PowerUp activePowerUp = null;

    //UI
    private Font gameFont;
    private Point waveTextPos;
    private Point scoreTextPos;
    private String waveTextPrefix;
    private String scoreTextPrefix;

    // Score
    private int scoreGotHit;
    private int scoreGotPowerup;
    private int scoreWaveCompleted;
    private int scoreHitProjectile;

    // Pause screen
    private final PauseScreen pauseScreen;

    // initialize
    public BattleScreen() {
        initAllGameObjects();
        initUIConfig();
        initScoreConfig();
        pauseScreen = new PauseScreen();
    }

    private void initAllGameObjects() {
        currentFrame = 0;
        score = 0;
        timeScale = 1;
        currentWave = 1;
        isInvincible = false;
        isPaused = false;

        spawnedEnemies.clear();
        spawnedPowerUps.clear();
        enemyList.clear();
        projectileList.clear();
        explosionList.clear();
        lifeIconList.clear();
        powerUpList.clear();
        activePowerUp = null;

        totalWaves = countTotalWaves();

        player = new Player();
        initLifeIcons();
    }

    private int countTotalWaves() {
        int wave = 1;
        while (true) {
            if (GameConfig.getInt("wave." + wave + ".enemy.0.arrivalTime") == null) {
                return wave - 1;
            }
            wave++;
        }
    }

    private void initUIConfig() {
        String fontPath = GameConfig.getString("text.font");
        int fontSize = GameConfig.getInt("text.size");
        gameFont = new Font(fontPath, fontSize);

        waveTextPrefix = GameConfig.getString("wave.text");
        scoreTextPrefix = GameConfig.getString("score.text");
        waveTextPos = GameConfig.getPoint("wave.pos");
        scoreTextPos = GameConfig.getPoint("score.pos");

        Window.setClearColour(0.0, 0.0, 0.0);
    }

    private void initScoreConfig() {
        scoreGotHit = GameConfig.getInt("score.gotHit");
        scoreGotPowerup = GameConfig.getInt("score.gotPowerup");
        scoreWaveCompleted = GameConfig.getInt("score.waveCompleted");
        scoreHitProjectile = GameConfig.getInt("score.hitProjectile");
    }

    private void initLifeIcons() {
        lifeIconList.clear();
        int initialLives = GameConfig.getInt("player.initialLives");
        String lifeImagePath = GameConfig.getString("playerLives.image");
        Point startPos = GameConfig.getPoint("playerLives.startPosition");
        double gap = GameConfig.getDouble("playerLives.gap");

        for (int i = 0; i < initialLives; i++) {
            double x = startPos.x + i * gap;
            lifeIconList.add(new LifeIcon(lifeImagePath, x, startPos.y));
        }
    }

    // update
    public GameState update(Input input) {
        handleGlobalKeys(input);

        if (isPaused) {
            drawAll();
            pauseScreen.draw();
            return null;
        }

        player.update(input);
        currentTimeScale = timeScale;

        // Timescale loop: runs multiple updates per frame when speed is increased
        for (int i = 0; i < timeScale; i++) {
            currentFrame++;

            spawnEnemies();
            spawnPowerUps();
            updateProjectiles(input);
            updateEnemies();
            updateExplosions();
            updatePowerUps();
            checkAllCollisions();

            if (!player.isAlive()) {
                return GameState.END_LOSE;
            }

            if (isWaveComplete()) {
                score += scoreWaveCompleted;
                if (currentWave >= totalWaves) {
                    return GameState.END_WIN;
                }
                currentWave++;
                spawnedEnemies.clear();
                spawnedPowerUps.clear();
                currentFrame = 0;
            }
        }

        drawAll();
        return null;
    }

    // control button
    private void handleGlobalKeys(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            isPaused = !isPaused;
        }
        if (input.wasPressed(Keys.G)) {
            timeScale++;
        }
        if (input.wasPressed(Keys.F)) {
            if (timeScale > 1) timeScale--;
        }
        if (input.wasPressed(Keys.I)) {
            isInvincible = !isInvincible;
            player.setInvincible(isInvincible);
        }
        if (input.wasPressed(Keys.R)) {
            initAllGameObjects();
        }

        if (input.wasPressed(Keys.N)) {
            skipToNextWave();
        }
    }

    private void skipToNextWave() {
        enemyList.clear();
        projectileList.clear();
        powerUpList.clear();

        score += scoreWaveCompleted;

        if (currentWave >= totalWaves) {
            return;
        }

        currentWave++;
        spawnedEnemies.clear();
        spawnedPowerUps.clear();
        currentFrame = 0;
    }

    // enemy spawn
    private boolean isEnemySpawned(String key) {
        return spawnedEnemies.contains(key);
    }

    private void markEnemyAsSpawned(String key) {
        if (!spawnedEnemies.contains(key)) {
            spawnedEnemies.add(key);
        }
    }

    private void spawnEnemies() {
        int enemyIndex = 0;
        while (true) {
            try {
                Integer arriveFrame = GameConfig.getInt(
                        "wave." + currentWave + ".enemy." + enemyIndex + ".arrivalTime"
                );
                if (arriveFrame == null) break;

                String enemyKey = currentWave + ".enemy." + enemyIndex;
                if (!isEnemySpawned(enemyKey) && currentFrame >= arriveFrame) {
                    String type = GameConfig.getString(
                            "wave." + currentWave + ".enemy." + enemyIndex + ".type"
                    );

                    Enemy enemy;
                    switch (type) {
                        case "regular":
                            enemy = new RegularEnemy(currentWave, enemyIndex);
                            break;
                        case "strafing":
                            enemy = new StrafingEnemy(currentWave, enemyIndex);
                            break;
                        case "shooting":
                            enemy = new ShootingEnemy(currentWave, enemyIndex);
                            break;
                        default:
                            enemy = new RegularEnemy(currentWave, enemyIndex);
                            break;
                    }

                    enemyList.add(enemy);
                    markEnemyAsSpawned(enemyKey);
                }
                enemyIndex++;
            } catch (Exception e) {
                break;
            }
        }
    }

    // powerup spawn
    private boolean isPowerUpSpawned(String key) {
        return spawnedPowerUps.contains(key);
    }

    private void markPowerUpAsSpawned(String key) {
        if (!spawnedPowerUps.contains(key)) {
            spawnedPowerUps.add(key);
        }
    }

    private void spawnPowerUps() {
        int powerUpIndex = 0;
        while (true) {
            try {
                Integer arriveFrame = GameConfig.getInt(
                        "wave." + currentWave + ".powerup." + powerUpIndex + ".arrivalTime"
                );
                if (arriveFrame == null) break;

                String powerUpKey = currentWave + ".powerup." + powerUpIndex;
                if (!isPowerUpSpawned(powerUpKey) && currentFrame >= arriveFrame) {
                    String type = GameConfig.getString(
                            "wave." + currentWave + ".powerup." + powerUpIndex + ".type"
                    );

                    PowerUp powerUp;
                    switch (type) {
                        case "shield":
                            powerUp = new ShieldPowerUp(currentWave, powerUpIndex);
                            break;
                        case "life":
                            powerUp = new LifePowerUp(currentWave, powerUpIndex);
                            break;
                        case "cooldown":
                            powerUp = new CooldownPowerUp(currentWave, powerUpIndex);
                            break;
                        case "engine":
                            powerUp = new EnginePowerUp(currentWave, powerUpIndex);
                            break;
                        default:
                            powerUpIndex++;
                            continue;
                    }

                    powerUpList.add(powerUp);
                    markPowerUpAsSpawned(powerUpKey);
                }
                powerUpIndex++;
            } catch (Exception e) {
                break;
            }
        }
    }

    // projectile update
    private void updateProjectiles(Input input) {
        // player shooting system
        if (input.wasPressed(Keys.SPACE) && player.canShoot() && !isPaused) {
            projectileList.add(
                    new PlayerProjectile(player.getCenterX(), player.getCenterY())
            );
        }

        Iterator<Projectile> iter = projectileList.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update();

            if (p instanceof PlayerProjectile &&
                    ((PlayerProjectile) p).isOutOfBounds()) {
                iter.remove();
            } else if (p instanceof EnemyProjectile &&
                    ((EnemyProjectile) p).isOutOfBounds()) {
                iter.remove();
            }
        }
    }

    //enemy update
    private void updateEnemies() {
        Iterator<Enemy> iter = enemyList.iterator();
        while (iter.hasNext()) {
            Enemy e = iter.next();
            e.update();

            // ShootingEnemy
            if (e instanceof ShootingEnemy) {
                ShootingEnemy shooter = (ShootingEnemy) e;
                if (shooter.shouldShoot()) {
                    projectileList.add(
                            new EnemyProjectile(e.getCenterX(), e.getCenterY())
                    );
                }
            }

            if (e.getY() > ShadowAliens.screenHeight) {
                iter.remove();
            }
        }
    }

    // explosion update
    private void updateExplosions() {
        Iterator<Explosion> iter = explosionList.iterator();
        while (iter.hasNext()) {
            Explosion exp = iter.next();
            exp.update();
            if (exp.isFinish()) {
                iter.remove();
            }
        }
    }

    // powerup update
    private void updatePowerUps() {
        if (activePowerUp != null && activePowerUp.isExpired()) {
            activePowerUp.removeEffect(player);
            activePowerUp = null;
        }

        Iterator<PowerUp> iter = powerUpList.iterator();
        while (iter.hasNext()) {
            PowerUp p = iter.next();
            p.update();
            if (p.isOutOfBounds() || p.isCollected()) {
                iter.remove();
            }
        }
    }

    // wave check
    private boolean isWaveComplete() {
        if (!enemyList.isEmpty()) return false;
        if (!powerUpList.isEmpty()) return false;

        for (Projectile p : projectileList) {
            if (p instanceof EnemyProjectile) return false;
        }

        int enemyIndex = 0;
        while (true) {
            Integer arriveFrame = GameConfig.getInt(
                    "wave." + currentWave + ".enemy." + enemyIndex + ".arrivalTime"
            );
            if (arriveFrame == null) break;
            String key = currentWave + ".enemy." + enemyIndex;
            if (!isEnemySpawned(key)) return false;
            enemyIndex++;
        }

        return true;
    }

    // crash check
    private void checkAllCollisions() {
        checkProjectileEnemyCollisions();
        checkPlayerEnemyCollisions();
        checkPlayerProjectileCollisions();
        checkPlayerPowerUpCollisions();
    }

    private void checkProjectileEnemyCollisions() {
        Iterator<Projectile> bulletIter = projectileList.iterator();
        while (bulletIter.hasNext()) {
            Projectile bullet = bulletIter.next();
            if (!(bullet instanceof PlayerProjectile)) continue;

            Rectangle bulletBox = bullet.getBoundingBox();
            Iterator<Enemy> enemyIter = enemyList.iterator();

            while (enemyIter.hasNext()) {
                Enemy enemy = enemyIter.next();
                if (bulletBox.intersects(enemy.getBoundingBox())) {

                    explosionList.add(
                            new Explosion(enemy.getCenterX(), enemy.getCenterY(), true)
                    );

                    String type = enemy.getType();
                    Integer pts = GameConfig.getInt("score.destroyedEnemy." + type);
                    if (pts != null) score += pts;

                    bulletIter.remove();
                    enemyIter.remove();
                    break;
                }
            }
        }
    }


    private void checkPlayerEnemyCollisions() {
        Rectangle playerBox = player.getBoundingBox();
        Iterator<Enemy> enemyIter = enemyList.iterator();

        while (enemyIter.hasNext()) {
            Enemy enemy = enemyIter.next();
            if (playerBox.intersects(enemy.getBoundingBox())) {
                explosionList.add(
                        new Explosion(enemy.getCenterX(), enemy.getCenterY(), true)
                );
                enemyIter.remove();

                if (!isInvincible && !player.isInvincible()) {
                    player.onHit();
                    score = Math.max(0, score - scoreGotHit);
                    removeLife();
                }
                break;
            }
        }
    }


    private void checkPlayerProjectileCollisions() {
        Rectangle playerBox = player.getBoundingBox();
        Iterator<Projectile> iter = projectileList.iterator();

        while (iter.hasNext()) {
            Projectile p = iter.next();
            if (!(p instanceof EnemyProjectile)) continue;

            if (playerBox.intersects(p.getBoundingBox())) {
                explosionList.add(
                        new Explosion(p.getX(), p.getY(), false)
                );
                iter.remove();

                if (!isInvincible && !player.isInvincible()) {
                    player.onHit();
                    score = Math.max(0, score - scoreGotHit);
                    removeLife();
                }
            }
        }
    }


    private void checkBulletBulletCollisions() {
        Iterator<Projectile> playerBulletIter = projectileList.iterator();
        while (playerBulletIter.hasNext()) {
            Projectile pb = playerBulletIter.next();
            if (!(pb instanceof PlayerProjectile)) continue;

            Iterator<Projectile> enemyBulletIter = projectileList.iterator();
            while (enemyBulletIter.hasNext()) {
                Projectile eb = enemyBulletIter.next();
                if (!(eb instanceof EnemyProjectile)) continue;

                if (pb.getBoundingBox().intersects(eb.getBoundingBox())) {
                    explosionList.add(
                            new Explosion(eb.getX(), eb.getY(), false)
                    );
                    score += scoreHitProjectile;
                    playerBulletIter.remove();
                    enemyBulletIter.remove();
                    break;
                }
            }
        }
    }


    private void checkPlayerPowerUpCollisions() {
        Rectangle playerBox = player.getBoundingBox();
        Iterator<PowerUp> iter = powerUpList.iterator();

        while (iter.hasNext()) {
            PowerUp p = iter.next();
            if (p.isCollected()) continue;

            if (playerBox.intersects(p.getBoundingBox())) {

                if (activePowerUp != null) {
                    activePowerUp.removeEffect(player);
                }
                p.collect(player);
                activePowerUp = p;
                score += scoreGotPowerup;
                iter.remove();
            }
        }
    }

    // life system
    private void removeLife() {
        if (!lifeIconList.isEmpty()) {
            lifeIconList.remove(lifeIconList.size() - 1);
        }
    }

    // draw
    public void drawAll() {
        Window.setClearColour(0, 0, 0);

        for (Explosion exp : explosionList) exp.draw();
        for (Enemy enemy : enemyList) enemy.draw();
        for (PowerUp p : powerUpList) p.draw();

        player.draw();

        for (Projectile bullet : projectileList) bullet.draw();
        for (LifeIcon life : lifeIconList) life.draw();

        String waveText = waveTextPrefix + " " + currentWave;
        gameFont.drawString(waveText, waveTextPos.x, waveTextPos.y);

        String scoreText = scoreTextPrefix + " " + score;
        gameFont.drawString(scoreText, scoreTextPos.x, scoreTextPos.y);
    }
}