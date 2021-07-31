package Input;

import Money.Entry;
import Phrases.Phrases;
import window.Window;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseAdapterEntry extends MouseAdapter {
    private final Window window;
    private final Entry entry;
    private final Panel panel;

    public MouseAdapterEntry(Window window, Panel panel, Entry entry) {
        this.window = window;
        this.panel = panel;
        this.entry = entry;
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
        if (window.isInputEmpty() || window.isEntryShown()) {
            window.showEntry(entry);
            window.edit(entry);
        }
    }
}
