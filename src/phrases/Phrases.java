package phrases;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Objects;

public class Phrases {

    private Phrases() {
    }

    // Fonts
    public static void init() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Phrases.class.getResourceAsStream("/arial.ttf"))));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    // Fonts
    public static final int fontSize = 13; // default = 12
    public static final Font showFontBold = new Font("arial", Font.BOLD, fontSize);
    public static final Font showFontPlain = new Font("arial", Font.PLAIN, fontSize);
    public static final Font inputFont = new Font("arial", Font.BOLD, fontSize);

    // The Text on th GUI
    public static final String number = "Nr.";
    public static final String date = "Datum";
    public static final String receiver = "Empfänger";
    public static final String category = "Kategorie";
    public static final String purpose = "V. Zweck";
    public static final String tableSpending = "Ausgaben";
    public static final String tableIncome = "Einnahmen";
    public static final String balance = "Saldo";
    public static final String neu = "Neu";
    public static final String edit = "Bearbeiten";
    public static final String enter = "Eintragen";
    public static final String cancel = "Abbrechen";
    public static final String by = "Von";
    public static final String value = "Betrag";
    // The Text on the menu bar
    public static final String options = "options";
    public static final String save = "save";
    public static final String saveUnder = "save under";
    public static final String exit = "exit";

    public static final Color BACKGROUND = Color.GRAY;
    public static final Color BACKGROUND_LIGHT = Color.LIGHT_GRAY;
    public static final Color FOREGROUND = Color.BLACK;
    public static final Color HIGHLIGHTS = BACKGROUND.darker();
    public static final Color BORDER = Color.BLACK.brighter();

    public static final Color COLOR_TABLE_CONTENT_BACKGROUND = BACKGROUND_LIGHT;
    public static final Color COLOR_TABLE_HEAD_ROW = BACKGROUND_LIGHT.darker(); // COLOR_CONTROL_BUTTONS = Color.LIGHT_GRAY.darker().darker();
    public static final Color COLOR_CONTROL_BUTTONS = BACKGROUND;
    public static final Color COLOR_CONTROL_INPUTS = BACKGROUND_LIGHT;
    public static final Color COLOR_BUTTON = BACKGROUND_LIGHT.darker();

    /**
     * The color for a positive balance
     */
    public static final Color normalFontColor = Color.BLACK;
    /**
     * The color for a negative balance
     */
    public static final Color minusFontColor = Color.RED;

    // input
    public static final String moneySymbol = "€";
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

    public static final String valueOutOfBounce = "Die Eingabe ist zu groß (max = " + new DecimalFormat("#,###.##").format(inputValueMax) + ")";
    public static final String invalidInput = "Falsche Eingabe";
    public static final String invalidInputChar = "Verbotene Zeichen verwendet";
    public static final String syntaxErrorInCalculationExpression = "Syntaxfehler im Ausdruck";
    public static final String calculationSuccessfulWithRounding = "Das Ergebniss wurde gerundet.";

    public static final String saveDialogTitle = "Speichern";
    public static final String saveDialogText = "Möchten Sie die Datei Speichern?";

    public static final String overrideWhenSavingTitle = "Datei überschreiben";
    public static final String overrideWhenSavingMessage = "Soll die Datei mit dem aktuellen stand überschrieben werden?";

    /**
     * The number of times a receiver, by, category or purpose must be used before it is shown be default in the drop down menu.
     */
    public static final int LIST_JUMP_VALUE = 8;

    public static final long periodForAnimatedIcon = 3; // time in milliseconds

    // calculator
    public static final int calculatorMinValue = 0;
    public static final int calculatorMaxValue = 9999999; // max input value because calculating with higher numbers is difficult. Written with exponent (eg. 3e7)

}