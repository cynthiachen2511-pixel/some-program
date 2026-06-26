package game.entity;

import bagel.Image;
import game.config.GameConfig;

public class Explosion {

    private final Image image;
    private final double x;
    private final double y;
    private final int duration;
    private int currentTimer;

   // true is big explosion, false is small explosion
    public Explosion(double x, double y, boolean isLarge) {
        if (isLarge) {
            image = new Image(GameConfig.getString("explosion.large.image"));
            duration = GameConfig.getInt("explosion.large.duration");
        } else {
            image = new Image(GameConfig.getString("explosion.small.image"));
            duration = GameConfig.getInt("explosion.small.duration");
        }

        this.x = x;
        this.y = y;
        this.currentTimer = duration;
    }

    public void update() {
        currentTimer--;
    }

    public void draw() {
        image.draw(x, y);
    }

    public boolean isFinish() {
        return currentTimer <= 0;
    }
}