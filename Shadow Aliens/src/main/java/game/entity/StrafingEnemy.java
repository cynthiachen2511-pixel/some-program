package game.entity;

import game.ShadowAliens;
import game.config.GameConfig;

public class StrafingEnemy extends Enemy {

    private int directionX; // 1 = 向右, -1 = 向左

    public StrafingEnemy(int wave, int index) {
        super(wave, index, "strafing");

        // decide which side should toward
        double screenCenter = ShadowAliens.screenWidth / 2.0;
        if (x < screenCenter) {
            // left
            directionX = -1;
        } else {
            // right
            directionX = 1;
        }
    }

    @Override
    public void update() {
        y += movementSpeed;

        x += movementSpeed * directionX;

        // check left confine
        double halfWidth = image.getWidth() / 2.0;
        if (x - halfWidth <= 0) {
            x = halfWidth;
            // rebound to the right
            directionX = 1;
        }

        // check right confine
        if (x + halfWidth >= ShadowAliens.screenWidth) {
            x = ShadowAliens.screenWidth - halfWidth;
            // rebound to the left
            directionX = -1;
        }
    }

    @Override
    public String getType() { return "strafing"; }
}