package utilitis;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.Timer;

public class CustomPopup {
    private final Popup popup;

    public CustomPopup(int x, int y, String message) {
        this(null, x, y, message, 2000, null);
    }

    public CustomPopup(Component owner, int x, int y, String message, long delay, Dimension minSize) {
        JTextArea content = new JTextArea(message);
        content.setBorder(new LineBorder(Color.BLACK, 1));
        content.setEditable(false);
        String longest_message_part = "";
        for (String s : message.split("\n")) {
            if (s.length() >= longest_message_part.length()) {
                longest_message_part = s;
            }
        }
        if (minSize != null) {
            minSize.width = Math.max(minSize.width, content.getFontMetrics(content.getFont()).stringWidth(longest_message_part) + 2);
            minSize.height = Math.max(minSize.height, content.getFontMetrics(content.getFont()).getHeight() * message.split("\n").length);
            content.setPreferredSize(minSize);
        }

        popup = PopupFactory.getSharedInstance().getPopup(owner, content, x, y);
        popup.show();
        if (delay > 0) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    close();
                }
            }, delay);
        } else {
            content.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    close();
                }
            });
            content.addMouseWheelListener(e -> popup.hide());
        }
    }

    public void close() {
        popup.hide();
    }
}
