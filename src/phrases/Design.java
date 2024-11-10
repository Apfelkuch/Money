package phrases;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Design {
    public static void init() {

        UIManager.put("PopupMenu.border", new LineBorder(Phrases.BORDER));
        UIManager.put("PopupMenu.background", Phrases.BACKGROUND);
        UIManager.put("PopupMenu.foreground", Phrases.FOREGROUND);

        UIManager.put("Menu.selectionBackground", Phrases.HIGHLIGHTS);
        UIManager.put("Menu.selectionForeground", Phrases.FOREGROUND);

        UIManager.put("MenuItem.selectionBackground", Phrases.HIGHLIGHTS);
        UIManager.put("MenuItem.selectionForeground", Phrases.FOREGROUND);

        UIManager.put("Separator.background", Phrases.BACKGROUND);
        UIManager.put("Separator.foreground", Phrases.FOREGROUND);

    }

}
