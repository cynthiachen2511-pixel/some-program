package game.entity;

import game.config.GameConfig;

public class ShootingEnemy extends Enemy {

    private final int firingRate;
    private final int arrivalTime;
    private int frameCount;

    public ShootingEnemy(int wave, int index) {
        super(wave, index, "shooting");

        firingRate = GameConfig.getInt("enemy.shooting.firingRate");
        arrivalTime = GameConfig.getInt(
                "wave." + wave + ".enemy." + index + ".arrivalTime"
        );
        frameCount = 0;
    }

    @Override
    public void update() {
        y += movementSpeed;
        frameCount++;
    }

    //check should shoot or not
    public boolean shouldShoot() {
        return frameCount > 0 && frameCount % firingRate == 0;
    }

    @Override
    public String getType() { return "shooting"; }
}