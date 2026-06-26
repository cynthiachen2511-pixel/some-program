package game.entity;

import game.ShadowAliens;

// Abstract base class for all projectile types in the game.
public abstract class Projectile extends GameObject {

    protected int movementSpeed;

    /**
     * Creates a new Projectile at the given position.
     * @param x the starting x position
     * @param y the starting y position
     */
    public Projectile(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns whether this projectile has left the screen bounds.
     * @return true if out of bounds
     */
    public abstract boolean isOutOfBounds();

    @Override
    public void draw() {
        image.draw(x, y);
    }

    public double getX() {
        return x;
    }
}