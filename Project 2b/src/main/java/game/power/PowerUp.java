package game.power;

import bagel.Image;
import bagel.util.Rectangle;
import game.ShadowAliens;
import game.config.GameConfig;
import game.entity.Player;

public abstract class PowerUp {

    protected Image image;
    protected double x;
    protected double y;
    protected int movementSpeed;
    protected int duration;
    protected int timer;
    protected boolean active;
    protected boolean collected;

    public PowerUp(int wave, int index, String type) {
        image = new Image(GameConfig.getString("powerup." + type + ".image"));
        movementSpeed = GameConfig.getInt("powerup." + type + ".movementSpeed");

        Integer dur = GameConfig.getInt("powerup." + type + ".duration");
        this.duration = (dur != null) ? dur : 0;

        this.x = GameConfig.getDouble("wave." + wave + ".powerup." + index + ".posX");
        this.y = -image.getHeight();

        this.active = false;
        this.collected = false;
        this.timer = 0;
    }

    public void update() {
        if (!collected) {
            y += movementSpeed;
        } else if (active) {
            timer--;
            if (timer <= 0) {
                active = false;
            }
        }
    }

    public void draw() {
        if (!collected) {
            image.draw(x, y);
        }
    }

    public void collect(Player player) {
        collected = true;
        active = true;
        timer = duration;
        applyEffect(player);
    }

    public void removeEffect(Player player) {
    }

    protected abstract void applyEffect(Player player);

    public boolean isOutOfBounds() {
        return y > ShadowAliens.screenHeight;
    }

    public boolean isCollected() {
        return collected;
    }

    public boolean isExpired() {
        return collected && !active;
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(
                x - image.getWidth() / 2,
                y - image.getHeight() / 2,
                image.getWidth(),
                image.getHeight()
        );
    }
}