package utilitis;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class CustomPopup {
    private JPopupMenu jPopupMenu;
    private boolean opaque;

    public CustomPopup() {
    }

    public CustomPopup(int x, int y, String message) {
        new CustomPopup(x, y, message, 2000);
    }

    public CustomPopup(int x, int y, String message, long delay) {
        buildPopup(x, y, message);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                jPopupMenu.setVisible(false);
            }
        }, delay);
    }

    public CustomPopup buildPopup(int x, int y, String message) {
        jPopupMenu = new JPopupMenu();
        JTextArea jTextArea = new JTextArea(message);
        jTextArea.setOpaque(opaque);
        jPopupMenu.add(jTextArea);
        jPopupMenu.setBorder(new LineBorder(Color.BLACK, 1));
        jPopupMenu.setLocation(x, y);
        jPopupMenu.setVisible(true);
        return this;
    }

    public void close() {
        jPopupMenu.setVisible(false);
    }

    public CustomPopup setOpaque(boolean opaque) {
        this.opaque = opaque;
        return this;
    }
}
