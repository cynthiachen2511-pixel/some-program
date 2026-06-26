package game.screen;

import bagel.Font;
import bagel.DrawOptions;
import bagel.util.Colour;
import bagel.util.Point;
import game.config.GameConfig;
import bagel.Window;

//Pause Screen, the operation introduction
public class PauseScreen {

    // title font
    // font configuration
    private final Font titleFont = new Font(
            GameConfig.getString("text.font"),
            GameConfig.getInt("pausedTitle.size")
    );
    private final Font textFont;

    // text
    private final String titleText;
    private final String instructionText;

    private final DrawOptions centerAlign;

    // Initialize text
    public PauseScreen() {
        // read the ui configuration
        Colour textColour = GameConfig.getColour("text.colour");

        // introduction font
        textFont = new Font(
                GameConfig.getString("text.font"),
                GameConfig.getInt("text.size")
        );

        // text
        titleText = GameConfig.getString("pausedTitle.text");
        instructionText = GameConfig.getString("controlsList.text");

        // keep in center
        centerAlign = new DrawOptions();
        centerAlign.setScale(1.0, 1.0);
    }

    //draw the pause screen
    public void draw() {
        // get the screen center
        double centerX = Window.getWidth() / 2.0;
        double centerY = Window.getHeight() / 2.0;

        // Title
        // make title in the middle
        double titleHalfWidth = titleFont.getWidth(titleText) / 2.0;
        // the Y value of title
        double titleY = GameConfig.getInt("pausedTitle.posY");
        // get the configuration
        titleFont.drawString(titleText, centerX - titleHalfWidth, titleY,  centerAlign);

        // Introduction
        String[] controlLines = instructionText.split(",");
        // make introduction in the middle
        double textHalfWidth = textFont.getWidth(instructionText) / 2.0;
        // the Y value of introduction
        double instructionY = GameConfig.getInt("controlsList.startPosY");
        // the gap between two lines
        final double lineGap = GameConfig.getInt("controlsList.rowGap");

        for (String line : controlLines) {
            double lineHalfWidth = textFont.getWidth(line) / 2.0;
            textFont.drawString(line, centerX - lineHalfWidth, instructionY, centerAlign);
            instructionY += lineGap;
        }
        

        // draw the speed text
        String timescaleText = GameConfig.getString("timescale.text");
        int speed = BattleScreen.currentTimeScale;
        String speedText = timescaleText + " " + speed;

        Point speedPos = GameConfig.getPoint("timescale.pos");
        textFont.drawString(speedText, speedPos.x, speedPos.y);

    }
}