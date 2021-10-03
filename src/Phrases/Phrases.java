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

    // Fonts
//    public static Font showFontBold = new Font("arial", Font.BOLD, 20);
    public static Font showFontBold = new Font("arial", Font.BOLD, 12);
    //    public static Font showFontPlain = new Font("arial", Font.PLAIN, 18);
    public static Font showFontPlain = new Font("arial", Font.PLAIN, 12);
    public static Font inputFont = new Font("arial", Font.BOLD, 12);

    // The Text on th GUI
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
    // The Text on the menu bar
    public static String options = "options";
    public static String save = "save";
    public static String exit = "exit";


    // color from Color pallets (source = 'https://www.canva.com/de_de/lernen/schoene-farbpaletten-und-farbkombinationen/')
    public static Color[] own = new Color[]
            {Color.LIGHT_GRAY, Color.decode("#3A5199") /*Blue*/,
                    Color.decode("#E1B80D"), Color.decode("#EFB509"), Color.decode("#D8990F"), Color.decode("#CD7213") /*Orange*/};


    public static Color MAIN_LAYER_BACKGROUND = null;

    public static Color COLOR_TABLE_BACKGROUND = Phrases.own[0];
    public static Color COLOR_TABLE_CONTENT_BACKGROUND = null;
    public static Color COLOR_TABLE_HEAD_ROW = COLOR_TABLE_BACKGROUND == null ? null : COLOR_TABLE_BACKGROUND.darker();
    public static Color COLOR_TABLE_SPLIT = Phrases.own[0] == null ? null : Phrases.own[0].darker().darker();
    public static Color COLOR_CONTROL_PANEL_BACKGROUND = Phrases.own[0];
    public static Color COLOR_CONTROL_BACKGROUND = null;
    public static Color COLOR_CONTROL_1 = Phrases.own[0].darker().darker();
    public static Color COLOR_CONTROL_2 = Phrases.own[0].darker().darker();
    public static Color COLOR_CONTROL_3 = Phrases.own[0];
    //    public static Color COLOR_BUTTON = Phrases.own[1].brighter();
    public static Color COLOR_BUTTON = Phrases.own[0].darker();

    /**
     * The color for a positive balance
     */
    public static Color normalFontColor = Color.BLACK;
    /**
     * The color for a negative balance
     */
    public static Color minusFontColor = Color.RED;

    // input
    public static String moneySymbol = "€";
    public static final int inputValueMax = Integer.parseInt("10000");
    public static final int inputValueMin = Integer.parseInt("-1");

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
    public static final String deleteEntryMessage = "Lösche Eintrag: ";
    public static Color EXTRA_WINDOW_FOREGROUND = Color.BLACK;
    public static Color EXTRA_WINDOW_BACKGROUND = Color.LIGHT_GRAY;

    public static String valueOutOfBounce = "Die Eingabe ist zu groß";
    public static String invalidInput = "Falsche Eingabe";
    public static String invalidInputChar = "Verbotene Zeichen verwendet.";


    /**
     * The number of times a receiver, by, category or purpose must be used before it is shown be default in the drop down menu.
     */
    public static final int LIST_JUMP_VALUE = 8;

}