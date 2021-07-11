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
        System.out.println(Phrases.font.getName());

    }

    public static Font font = new Font("arial", Font.BOLD, 12);

    public static String number = "Nr.";
    public static String date = "Datum";
    public static String receiver = "Empfänger";
    public static String category = "Kategorie";
    public static String purpose = "V. Zweck";
    public static String cost = "Ausgaben";
    public static String gain = "Einnahmen";
    public static String balance = "Saldo";
    public static String neu = "Neu";
    public static String edit = "Bearbeiten";
    public static String enter = "Eintragen";
    public static String cancel = "Abbrechen";
    public static String by = "Von";
    public static String value = "Betrag";

}
