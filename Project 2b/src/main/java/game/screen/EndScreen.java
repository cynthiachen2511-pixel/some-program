package game.screen;

import bagel.*;
import bagel.util.Point;
import game.ShadowAliens;
import game.config.GameConfig;
import game.entity.Player;

public class EndScreen {

    private final Font instructionFont;
    private final String[] instructionLines;
    private final double instructionStartY;
    private final double instructionRowGap;

    private final Font winFont;
    private final String winText;
    private final double winY;

    private final Font loseFont;
    private final String loseText;
    private final double loseY;

    private boolean isWin;

    private final Player player;

    public EndScreen() {
        String fontPath = GameConfig.getString("text.font");

        // Win configuration
        int winSize = GameConfig.getInt("end.win.size");
        winFont = new Font(fontPath, winSize);
        winText = GameConfig.getString("end.win.text");
        winY = GameConfig.getDouble("end.win.posY");

        // Lose configuration
        int loseSize = GameConfig.getInt("end.lose.size");
        loseFont = new Font(fontPath, loseSize);
        loseText = GameConfig.getString("end.lose.text");
        loseY = GameConfig.getDouble("end.lose.posY");

        // Instruction
        int instructionSize = GameConfig.getInt("text.size");
        instructionFont = new Font(fontPath, instructionSize);
        String instructionsRaw = GameConfig.getString("end.instructionsList.text");
        instructionLines = instructionsRaw.split(",");
        instructionStartY = GameConfig.getDouble("end.instructionsList.startPosY");
        instructionRowGap = GameConfig.getDouble("end.instructionsList.rowGap");

        player = new Player();
    }

    // Set the state is win or lose
    public void setWin(boolean isWin) {
        this.isWin = isWin;
    }

    // end screen update
    public boolean update(Input input) {
        player.updateMovementOnly(input);

        draw();

        // press SPACE to restart
        return input.wasPressed(Keys.SPACE);
    }

    private void draw() {
        Window.setClearColour(0, 0, 0);

        player.draw();

        double centerX = ShadowAliens.screenWidth / 2.0;

        // show text when win/lose
        if (isWin) {
            double x = centerX - winFont.getWidth(winText) / 2.0;
            winFont.drawString(winText, x, winY);
        } else {
            double x = centerX - loseFont.getWidth(loseText) / 2.0;
            loseFont.drawString(loseText, x, loseY);
        }

        // instruction text
        double y = instructionStartY;
        for (String line : instructionLines) {
            double lineX = centerX - instructionFont.getWidth(line) / 2.0;
            instructionFont.drawString(line, lineX, y);
            y += instructionRowGap;
        }
    }
}