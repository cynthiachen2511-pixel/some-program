package game.power;

import game.entity.Player;

public class CooldownPowerUp extends PowerUp {

    private int originalCooldown;
    private static final int COOLDOWN_DIVISOR = 3;
    private static final int MIN_COOLDOWN = 1;

    public CooldownPowerUp(int wave, int index) {
        super(wave, index, "cooldown");
    }

    @Override
    protected void applyEffect(Player player) {
        originalCooldown = player.getShootCooldown();
        int newCooldown = Math.max(MIN_COOLDOWN, originalCooldown / COOLDOWN_DIVISOR);
        player.setShootCooldown(newCooldown);
    }

    @Override
    public void removeEffect(Player player) {
        player.setShootCooldown(originalCooldown);
    }
}