package game.entity;

import bagel.Image;
import game.ShadowAliens;
import game.config.GameConfig;

public class EnemyProjectile extends Projectile {

    // create a new enemy projectile
    public EnemyProjectile(double x, double y) {
        super(x, y);
        image = new Image(GameConfig.getString("enemyProjectile.image"));
        movementSpeed = GameConfig.getInt("enemyProjectile.movementSpeed");
    }

    @Override
    public void update() {
        y += movementSpeed;
    }
    
    @Override
    public boolean isOutOfBounds() {
        return y > ShadowAliens.screenHeight;
    }
}