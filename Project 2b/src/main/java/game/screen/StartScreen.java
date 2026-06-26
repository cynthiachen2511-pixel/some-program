package game.screen;

import bagel.*;
import bagel.util.Point;
import game.ShadowAliens;
import game.config.GameConfig;
import game.entity.Player;

public class StartScreen {

    private final Font titleFont;
    private final Font instructionFont;
    private final String titleText;
    private final String[] instructionLines;
    private final double titleY;
    private final double instructionStartY;
    private final double instructionRowGap;

    private final Player player;

    public StartScreen() {
        String fontPath = GameConfig.getString("text.font");
        int titleSize = GameConfig.getInt("start.title.size");
        int instructionSize = GameConfig.getInt("text.size");

        titleFont = new Font(fontPath, titleSize);
        instructionFont = new Font(fontPath, instructionSize);

        titleText = GameConfig.getString("start.title.text");
        titleY = GameConfig.getDouble("start.title.posY");

        // read the text
        String instructionsRaw = GameConfig.getString("start.instructionsList.text");
        instructionLines = instructionsRaw.split(",");
        instructionStartY = GameConfig.getDouble("start.instructionsList.startPosY");
        instructionRowGap = GameConfig.getDouble("start.instructionsList.rowGap");

        player = new Player();
    }

    // update start screen
    public boolean update(Input input) {
        player.updateMovementOnly(input);

        draw();

        return input.wasPressed(Keys.SPACE);
    }

    // draw
    private void draw() {
        Window.setClearColour(0, 0, 0);

        player.draw();

        double centerX = ShadowAliens.screenWidth / 2.0;
        double titleX = centerX - titleFont.getWidth(titleText) / 2.0;
        titleFont.drawString(titleText, titleX, titleY);

        double y = instructionStartY;
        for (String line : instructionLines) {
            double lineX = centerX - instructionFont.getWidth(line) / 2.0;
            instructionFont.drawString(line, lineX, y);
            y += instructionRowGap;
        }
    }
}