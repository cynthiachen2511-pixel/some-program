package game.entity;

import bagel.Image;
import game.config.GameConfig;

public class LifeIcon {

    private final Image image;
    private final double x;
    private final double y;

    //===================== Initialize lifeIcon =====================
    public LifeIcon(String lifeImagePath, double x, double y) {
        this.image = new Image(lifeImagePath);
        this.x = x;
        this.y = y;
    }

    // ===================== draw the lifeIcon =====================
    public void draw() {
        image.draw(x, y);
    }
}