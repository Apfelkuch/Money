package Input;

import Money.Entry;
import Money.Money;
import Phrases.Phrases;
import window.Window;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseAdapterEntry extends MouseAdapter {
    private final Window window;
    private final Entry entry;
    private final JPanel panel;
    private final Money money;

    public MouseAdapterEntry(Window window, JPanel panel, Entry entry, Money money) {
        this.window = window;
        this.panel = panel;
        this.entry = entry;
        this.money = money;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        panel.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND.darker());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        panel.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
        if (e.getButton() == MouseEvent.BUTTON1 && (window.isInputEmpty() || window.isEntryShown())) {
            window.showEntry(entry);
            window.edit(entry);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
//            int result = ExtraWindow.confirmDialog(window, Phrases.deleteEntry, Phrases.deleteEntryMessage + entry.getNumber(), Phrases.inputFont, Phrases.EXTRA_WINDOW_BACKGROUND, Phrases.EXTRA_WINDOW_FOREGROUND);
            int result = JOptionPane.showConfirmDialog(window, "Will you delete Entry: " + entry.getNumber());
            if (result == JOptionPane.YES_OPTION) {
                money.updateNumbers(entry);
                money.getEntries().remove(entry);
                money.updateAllEntries();
            }
        }
    }
}
