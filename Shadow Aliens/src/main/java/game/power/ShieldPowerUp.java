package game.power;

import game.entity.Player;

public class ShieldPowerUp extends PowerUp {

    public ShieldPowerUp(int wave, int index) {
        super(wave, index, "shield");
    }

    @Override
    protected void applyEffect(Player player) {
        player.setInvincible(true);
    }

    @Override
    public void removeEffect(Player player) {
        player.setInvincible(false);
    }
}