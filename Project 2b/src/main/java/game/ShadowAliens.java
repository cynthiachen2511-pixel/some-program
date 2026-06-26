package game;

import bagel.AbstractGame;
import bagel.Input;
import game.config.GameConfig;
import game.screen.BattleScreen;
import game.screen.StartScreen;
import game.screen.EndScreen;

import java.util.Properties;

public class ShadowAliens extends AbstractGame {
    public static Properties gameProps;
    public static double screenWidth;
    public static double screenHeight;

    private GameState gameState;

    private StartScreen startScreen;
    private BattleScreen battleScreen;
    private EndScreen endScreen;

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        ShadowAliens.gameProps = gameProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        GameConfig.init(gameProps);

        // initialize all the screen
        startScreen = new StartScreen();
        battleScreen = new BattleScreen();
        endScreen = new EndScreen();

        gameState = GameState.START;
    }

    @Override
    protected void update(Input input) {
        switch (gameState) {
            case START:
                if (startScreen.update(input)) {
                    gameState = GameState.BATTLE;
                }
                break;

            case BATTLE:
                GameState result = battleScreen.update(input);
                if (result != null) {
                    gameState = result;
                    if (result == GameState.END_WIN || result == GameState.END_LOSE) {
                        endScreen.setWin(result == GameState.END_WIN);
                    }
                }
                break;

            case END_WIN:
            case END_LOSE:
                if (endScreen.update(input)) {
                    battleScreen = new BattleScreen();
                    gameState = GameState.BATTLE;
                }
                break;
        }
    }

    public static void main(String[] args) {
        String configPath = System.getProperty("gameData", "gameData.properties");
        Properties gameProps = IOUtils.readPropertiesFile(configPath);
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}