package game.power;

import game.entity.Player;

public class EnginePowerUp extends PowerUp {

    private double originalSpeed;
    private static final int SPEED_MULTIPLIER = 2;


    public EnginePowerUp(int wave, int index) {
        super(wave, index, "engine");
    }

    @Override
    protected void applyEffect(Player player) {
        originalSpeed = player.getSpeed();
        player.setSpeed(originalSpeed * SPEED_MULTIPLIER);
    }

    @Override
    public void removeEffect(Player player) {
        player.setSpeed(originalSpeed);
    }
}