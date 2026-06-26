package game.power;

import game.entity.Player;

public class LifePowerUp extends PowerUp {

    public LifePowerUp(int wave, int index) {
        super(wave, index, "life");
    }

    @Override
    protected void applyEffect(Player player) {
        player.addLife();
    }

    @Override
    public void removeEffect(Player player) {
    }
}