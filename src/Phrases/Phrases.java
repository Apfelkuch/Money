package Phrases;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Phrases {

    // Fonts
    public Phrases() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/arial.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
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
    public static Color COLOR_CONTROL_BACKGROUND = Color.LIGHT_GRAY;
    public static Color COLOR_TABLE_BACKGROUND = Color.LIGHT_GRAY;
    public static Color COLOR_TABLE_CONTENT_BACKGROUND = Color.LIGHT_GRAY;

    public static Color normalFontColor = Color.BLACK;
    public static Color minusFontColor = Color.RED;

    // input
    public static String moneySymbol = "€";

    // save and load
    public static final char DIVIDER = (char) 160;
    public static final char PLACEHOLDER = (char) 177;
    public static final String CONTROL_VALUE = "Version 1" + Phrases.DIVIDER + Phrases.DIVIDER;
    public static final String PATH = System.getProperties().getProperty("user.dir") + "\\Money";
    public static final String FILENAME = "save.money";
    public static final String FILE_PATHS = "paths.money";
    public static final String choseFile = "Wähle eine Datei";
    public static final String startNew = "Neu starten";
    public static final String searchFile = "Suche ein File";
    public static final String deletePaths = "Lösche alle Dateivorschläge";

    public static final String deleteEntry = "Eintrag löschen";
    public static final String deleteEntryMessage = "Bestätige den Eintrag zu löschen.\nNummer: ";
    public static Color EXTRA_WINDOW_FOREGROUND = Color.BLACK;
    public static Color EXTRA_WINDOW_BACKGROUND = Color.LIGHT_GRAY;

    public static String valueOutOfBounce = "Die Eingabe ist zu groß";
    public static String invalidInput = "Falsche Eingabe";
    public static String invalidInputChar = "Verbotene Zeichen verwendet.";


    public static final int LIST_JUMP_VALUE = 8;

}