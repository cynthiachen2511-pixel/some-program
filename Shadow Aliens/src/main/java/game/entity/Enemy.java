package game.entity;

import bagel.DrawOptions;
import bagel.Image;
import game.ShadowAliens;
import game.config.GameConfig;

// Abstract base class for all enemy types in the game.
public abstract class Enemy extends GameObject {

    protected int movementSpeed;
    protected int wave;
    protected int index;
    protected String type;

    /**
     * Creates a new Enemy from wave configuration.
     * @param wave the wave number
     * @param index the enemy index within the wave
     * @param type the enemy type string
     */
    public Enemy(int wave, int index, String type) {
        this.type = type;
        image = new Image(GameConfig.getString("enemy." + type + ".image"));
        this.x = GameConfig.getDouble("wave." + wave + ".enemy." + index + ".posX");
        this.movementSpeed = GameConfig.getInt("wave." + wave + ".enemy." + index + ".movementSpeed");
        this.wave = wave;
        this.index = index;
        this.y = -image.getHeight();
    }

    @Override
    public void draw() {

        image.draw(x, y, new DrawOptions().setRotation(Math.PI / 2));
    }

    public String getType() {

        return type;
    }

    /**
     * Returns a projectile if this enemy shoots this frame, null otherwise.
     * Default implementation returns null — subclasses override to shoot.
     * @return EnemyProjectile or null
     */
    public EnemyProjectile shoot() {

        return null;
    }
}