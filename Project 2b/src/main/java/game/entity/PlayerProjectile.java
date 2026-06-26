package game.entity;

import bagel.Image;
import game.config.GameConfig;


public class PlayerProjectile extends Projectile {

    // create a new player projectile
    public PlayerProjectile(double x, double y) {
        super(x, y);
        image = new Image(GameConfig.getString("projectile.image"));
        movementSpeed = GameConfig.getInt("projectile.movementSpeed");
    }

    @Override
    public void update() {
        y -= movementSpeed;
    }

    @Override
    public boolean isOutOfBounds() {
        return y < 0;
    }
}