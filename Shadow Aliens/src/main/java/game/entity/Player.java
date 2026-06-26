package game.entity;

import bagel.Input;
import bagel.Keys;
import bagel.util.Rectangle;
import game.ShadowAliens;
import game.config.GameConfig;

public class Player extends GameObject {

    private double speed;
    private double originalSpeed;

    private int shootCooldown;
    private int originalShootCooldown;
    private int cooldownTimer;

    private int initialLives;
    private int currentLives;

    private boolean invincible;
    private boolean shieldActive;
    private int hitInvincibilityTimer;
    private final int hitInvincibilityTime;
    private bagel.Image invincibilityImage;

    // create a new player and initiable
    public Player() {
        image = new bagel.Image(GameConfig.getString("player.image"));
        x = ShadowAliens.screenWidth / 2.0;
        y = GameConfig.getDouble("player.posY");
        speed = GameConfig.getDouble("player.speed");
        originalSpeed = speed;

        shootCooldown = GameConfig.getInt("player.shootCooldown");
        originalShootCooldown = shootCooldown;
        cooldownTimer = 0;

        initialLives = GameConfig.getInt("player.initialLives");
        currentLives = initialLives;

        invincible = false;
        shieldActive = false;
        hitInvincibilityTime = GameConfig.getInt("player.hitInvincibilityTime");
        hitInvincibilityTimer = 0;

        invincibilityImage = new bagel.Image(GameConfig.getString("invincibility.image"));
    }

    // update
    public void update(Input input) {
        move(input);
        cooldownTimer--;

        // Shield and hit invincibility tracked separately to avoid
        // cancelling shield when hit invincibility expires
        if (hitInvincibilityTimer > 0) {
            hitInvincibilityTimer--;
            if (hitInvincibilityTimer == 0 && !shieldActive) {
                invincible = false;
            }
        }
    }

    @Override
    public void update() {
    }

    public void updateMovementOnly(Input input) {

        move(input);
    }

    // movement
    private void move(Input input) {
        boolean movingLeft = input.isDown(Keys.A);
        boolean movingRight = input.isDown(Keys.D);

        if (movingLeft && movingRight) return;

        if (movingLeft && canMoveLeft()) {
            x -= speed;
        }
        if (movingRight && canMoveRight()) {
            x += speed;
        }
    }

    private boolean canMoveLeft() {

        return x - image.getWidth() / 2 > 0;
    }

    private boolean canMoveRight() {

        return x + image.getWidth() / 2 < ShadowAliens.screenWidth;
    }

    // shooting system
    public boolean canShoot() {
        if (cooldownTimer <= 0) {
            cooldownTimer = shootCooldown;
            return true;
        }
        return false;
    }

    // Life system
    public void addLife() {
        if (currentLives < initialLives) {
            currentLives++;
        }
    }

    public void onHit() {
        currentLives--;
        hitInvincibilityTimer = hitInvincibilityTime;
        invincible = true;
    }

    public boolean isAlive() {

        return currentLives > 0;
    }

    public int getCurrentLives() {

        return currentLives;
    }

    public int getInitialLives() {

        return initialLives;
    }

    // Invincible system
    public void setInvincible(boolean invincible) {
        this.shieldActive = invincible;
        this.invincible = invincible;
    }

    public boolean isInvincible() {

        return invincible;
    }

    // powerup
    public int getShootCooldown() {

        return shootCooldown;
    }

    public void setShootCooldown(int cooldown) {

        this.shootCooldown = cooldown;
    }

    public double getSpeed() {

        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // draw
    @Override
    public void draw() {
        image.draw(x, y);
        if (invincible) {
            invincibilityImage.draw(x, y);
        }
    }

    // crash system
    @Override
    public double getCenterY() {

        return y - image.getHeight() / 2;
    }
}