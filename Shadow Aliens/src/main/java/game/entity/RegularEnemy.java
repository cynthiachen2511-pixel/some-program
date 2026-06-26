package game.entity;

import game.ShadowAliens;
import game.config.GameConfig;

public class RegularEnemy extends Enemy {

    public RegularEnemy(int wave, int index) {
        super(wave, index, "regular");
    }

    @Override
    public void update() {
        y += movementSpeed;
    }

    @Override
    public String getType() { return "regular"; }
}