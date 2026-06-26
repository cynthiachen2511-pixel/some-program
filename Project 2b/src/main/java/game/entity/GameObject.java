package game.entity;

import bagel.Image;
import bagel.util.Rectangle;

//Abstract base class for all game objects that have a position and image.
public abstract class GameObject {

    protected Image image;
    protected double x;
    protected double y;

    public abstract void update();

    public abstract void draw();


    /**
     * Returns the bounding box for collision detection.
     * @return Rectangle representing the object's bounds
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(
                x - image.getWidth() / 2,
                y - image.getHeight() / 2,
                image.getWidth(),
                image.getHeight()
        );
    }

    public double getCenterX() {
        return x;
    }

    public double getCenterY() {
        return y;
    }

    public double getY() {
        return y;
    }
}

