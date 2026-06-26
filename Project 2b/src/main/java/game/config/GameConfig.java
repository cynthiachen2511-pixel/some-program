package game.config;
// the utility package
import bagel.util.Colour;
import bagel.util.Point;
import java.util.Properties;

public class GameConfig {

    private static Properties props;

    // Get the Configuration from ShadowAliens
    public static void init(Properties gameProps) {
        props = gameProps;
        if (props == null) {
            System.err.println("Error of gameData.properties");
            System.exit(-1);
        }
    }

    // ================ read the data ==================

    // Encapsulation of getString Method
    public static String getString(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            return null; // 找不到key返回null，让上层处理
        }
        return value.trim();
    }

    // Encapsulation of getInt Method
    public static Integer getInt(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }
        return Integer.parseInt(value);
    }

    public static Double getDouble(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }
        return Double.parseDouble(value);
    }

    // Encapsulation of getColor Method
    public static Colour getColour(String key) {
        String[] rgbStr = getString(key).split(",");
        // get the value of red, green, blue
        double r = Double.parseDouble(rgbStr[0].trim());
        double g = Double.parseDouble(rgbStr[1].trim());
        double b = Double.parseDouble(rgbStr[2].trim());
        //return new Colour(r, g, b);
        return new Colour(r / 255.0, g / 255.0, b / 255.0);
    }

    // Encapsulation of getPoint Method
    public static Point getPoint(String key) {
        // read the point data and use "," to split it
        String[] xyStr = getString(key).split(",");
        // get the x value and y value
        double x = Double.parseDouble(xyStr[0].trim());
        double y = Double.parseDouble(xyStr[1].trim());
        return new Point(x, y);
    }

    // 1. the index of enemy
    // 2. property of enemy
    public static Integer getEnemyInt(int index, String subKey) {
        return getInt("enemy." + index + "." + subKey);
    }

    public static Double getEnemyDouble(int index, String subKey) {
        return getDouble("enemy." + index + "." + subKey);
    }
}