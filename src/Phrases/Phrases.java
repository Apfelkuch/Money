package Phrases;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Phrases {

    // Font
    public Phrases() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/arial.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return;
        }

    }


    public static Font showFontBold = new Font("arial", Font.BOLD, 20);
    public static Font showFontPlain = new Font("arial", Font.PLAIN, 18);
    public static Font inputFont = new Font("arial", Font.BOLD, 12);

    public static String number = "Nr.";
    public static String date = "Datum";
    public static String receiver = "Empfänger";
    public static String category = "Kategorie";
    public static String purpose = "V. Zweck";
    public static String tableSpending = "Ausgaben";
    public static String tableIncome = "Einnahmen";
    public static String balance = "Saldo";
    public static String neu = "Neu";
    public static String edit = "Bearbeiten";
    public static String enter = "Eintragen";
    public static String cancel = "Abbrechen";
    public static String by = "Von";
    public static String value = "Betrag";
    // menu bar
    public static String options = "options";
    public static String save = "save";
    public static String exit = "exit";

    public static Color COLOR_HEAD_ROW = Color.GRAY;
    public static Color COLOR_CONTROLS = Color.GRAY;
    public static Color COLOR_CONTROLS_INPUT = Color.LIGHT_GRAY;
    public static Color COLOR_CONTROL_BACKGROUND = null;
    public static Color COLOR_TABLE_BACKGROUND = null;
    public static Color COLOR_TABLE_CONTENT_BACKGROUND = Color.LIGHT_GRAY;

}
