package Phrases;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Design {
    public static void init() {

        UIManager.put("PopupMenu.border", new LineBorder(Phrases.BORDER));
        UIManager.put("PopupMenu.background", Phrases.BACKGROUND);
        UIManager.put("PopupMenu.foreground", Phrases.FOREGROUND);

        UIManager.put("Menu.background", Phrases.BACKGROUND);
        UIManager.put("Menu.foreground", Phrases.FOREGROUND);
        UIManager.put("Menu.selectionBackground", Phrases.HIGHLIGHTS);
        UIManager.put("Menu.selectionForeground", Phrases.FOREGROUND);

        UIManager.put("MenuItem.border", new LineBorder(Phrases.BACKGROUND));
        UIManager.put("MenuItem.background", Phrases.BACKGROUND);
        UIManager.put("MenuItem.foreground", Phrases.FOREGROUND);
        UIManager.put("MenuItem.selectionBackground", Phrases.HIGHLIGHTS);
        UIManager.put("MenuItem.selectionForeground", Phrases.FOREGROUND);

        UIManager.put("Separator.background", Phrases.BACKGROUND);
        UIManager.put("Separator.foreground", Phrases.FOREGROUND);

        UIManager.put("MenuBar.border", new LineBorder(Phrases.BACKGROUND));
        UIManager.put("MenuBar.background", Phrases.BACKGROUND);
        UIManager.put("MenuBar.foreground", Phrases.FOREGROUND);

        UIManager.put("Label.foreground", Phrases.FOREGROUND);

        UIManager.put("Button.background", Phrases.BACKGROUND);
        UIManager.put("Button.foreground", Phrases.FOREGROUND);
        UIManager.put("Button.select", Color.GREEN);

    }

    public static void update(JComponent... components) {
        for (JComponent component : components) {
            component.updateUI();
        }
    }

}
