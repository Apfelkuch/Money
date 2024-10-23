package Phrases;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class Phrases {

    private Phrases() {
    }

    // Fonts
    public static void init() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/arial.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        setDefaultColors();
    }

    // Fonts
    public static int fontSize = 13; // default = 12
    public static Font showFontBold = new Font("arial", Font.BOLD, fontSize);
    public static Font showFontPlain = new Font("arial", Font.PLAIN, fontSize);
    public static Font inputFont = new Font("arial", Font.BOLD, fontSize);

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
    public static String saveUnder = "save under";
    public static String exit = "exit";


    // color from Color pallets (source = 'https://www.canva.com/de_de/lernen/schoene-farbpaletten-und-farbkombinationen/')
    public static Color[] own = new Color[]
            {Color.LIGHT_GRAY, Color.decode("#3A5199") /*Blue*/,
                    Color.decode("#E1B80D"), Color.decode("#EFB509"), Color.decode("#D8990F"), Color.decode("#CD7213") /*Orange*/};


    public static Color COLOR_TABLE_CONTENT_BACKGROUND;
    public static Color COLOR_TABLE_HEAD_ROW;
    public static Color COLOR_TABLE_SPLIT;
    public static Color COLOR_CONTROL_PANEL_BACKGROUND;
    public static Color COLOR_CONTROL_BACKGROUND;
    public static Color COLOR_CONTROL_1;
    public static Color COLOR_CONTROL_2;
    public static Color COLOR_CONTROL_3;
    public static Color COLOR_BUTTON;

    public static Color BACKGROUND;
    public static Color BACKGROUND_LIGHT;
    public static Color FOREGROUND;
    public static Color HIGHLIGHTS;
    public static Color BORDER;

    public static void setDefaultColors() {
        COLOR_TABLE_CONTENT_BACKGROUND = Phrases.own[0];
        COLOR_TABLE_HEAD_ROW = COLOR_TABLE_CONTENT_BACKGROUND == null ? null : COLOR_TABLE_CONTENT_BACKGROUND.darker();
        COLOR_TABLE_SPLIT = Phrases.own[0] == null ? null : Phrases.own[0].darker();
        assert Phrases.own[0] != null;
        COLOR_CONTROL_PANEL_BACKGROUND = Phrases.own[0].darker();
        COLOR_CONTROL_BACKGROUND = null;
        COLOR_CONTROL_1 = Phrases.own[0].darker().darker();
        COLOR_CONTROL_2 = Phrases.own[0].darker().darker();
        COLOR_CONTROL_3 = Phrases.own[0];
        COLOR_BUTTON = Phrases.own[0].darker();

        BACKGROUND = Color.GRAY;
        FOREGROUND = Color.BLACK;
        HIGHLIGHTS = BACKGROUND.darker();
        BORDER = Color.BLACK.brighter();
        BACKGROUND_LIGHT = Color.LIGHT_GRAY;
    }

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
    public static final int inputValueMax = Integer.parseInt("1000000");
    public static final int inputValueMin = Integer.parseInt("0");

    // save and load
    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;
    public static final int ENTRY_SETS = 10000;

    public static final char DIVIDER = (char) 160; // 1 = divide , 2 = divide controlValue from Content, 3 = divide entries in 1000 packs
    public static final char PLACEHOLDER = (char) 177;
    public static final String CONTROL_VALUE = "Version 1" + Phrases.DIVIDER + Phrases.DIVIDER;
    public static final String EXTENSION_MONEY = "money";
    public static final String EXTENSION_MONEY_TEXT = "Money (*.money)";
    public static final String FILE_PATHS = System.getProperties().getProperty("user.dir") + "\\Money\\paths.money";
    public static final String choseFile = "Wähle eine Datei";
    public static final String startNew = "Neu starten";
    public static final String searchFile = "Suche ein File";
    public static final String deletePaths = "Lösche alle Dateivorschläge";

    public static final String deleteEntry = "Eintrag löschen";
    public static final String deleteEntryMessage = "Lösche Eintrag: ";
    public static Color EXTRA_WINDOW_FOREGROUND = Color.BLACK;
    public static Color EXTRA_WINDOW_BACKGROUND = Color.LIGHT_GRAY;

    public static String valueOutOfBounce = "Die Eingabe ist zu groß (max = " + new DecimalFormat("#,###.##").format(inputValueMax) + ")";
    public static String invalidInput = "Falsche Eingabe";
    public static String invalidInputChar = "Verbotene Zeichen verwendet";
    public static String syntaxErrorInCalculationExpression = "Syntaxfehler im Ausdruck";
    public static String calculationSuccessfulWithRounding = "Das Ergebniss wurde gerundet.";

    public static String saveDialogTitle = "Saving";
    public static String saveDialogText = "Do you want to save the file?";

    public static String overrideWhenSavingTitle = "Datei überschreiben";
    public static String overrideWhenSavingMessage = "Soll die Datei mit dem aktuellen stand überschrieben werden?";

    // settings
    public static String settings = "Einstellungen";
    public static String colorTable = "Hintergrund der Einträge";
    public static String colorTableHeadRow = "Einträge Kopfzeile";
    public static String colorSplit = "Trennung von Eingabe und Einträgen";
    public static String colorControl = "Hintergrund der Eingabe";
    public static String colorControl1 = "Hintergrund Eingabe 1";
    public static String colorControl2 = "Hintergrund Eingabe 2";
    public static String colorControl3 = "Hintergrund Eingabe 3";
    public static String colorButton = "Knopffarbe";
    public static String use = "use";
    public static String normal = "default";

    /**
     * The number of times a receiver, by, category or purpose must be used before it is shown be default in the drop down menu.
     */
    public static final int LIST_JUMP_VALUE = 8;

    public static long periodForAnimatedIcon = 3;

    // calculator
    public static int calculatorMinValue = 0;
    public static int calculatorMaxValue = 9999999; // max input value because calculating with higher numbers is difficult. Written with exponent (eg. 3e7)

}