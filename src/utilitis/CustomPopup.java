package utilitis;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class CustomPopup {
    private final Popup popup;

    public CustomPopup(int x, int y, String message) {
        this(null, x, y, message, 2000, null);
    }

    public CustomPopup(Component owner, int x, int y, String message, long delay, Dimension minSize) {
        JTextArea jTextArea = new JTextArea(message);
        jTextArea.setBorder(new LineBorder(Color.BLACK,1));
        String longest_message_part = "";
        for (String s : message.split("\n")) {
            if (s.length() >= longest_message_part.length()) {
                longest_message_part = s;
            }
        }
        if (minSize != null) {
            minSize.width = Math.max(minSize.width, (jTextArea.getFontMetrics(jTextArea.getFont()).stringWidth(longest_message_part) + 2)); // Adjust the width of the popup.
            jTextArea.setPreferredSize(minSize);
        }

        popup = PopupFactory.getSharedInstance().getPopup(owner, jTextArea, x, y);
        popup.show();
        if (delay > 0) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
//                    System.out.println("TIMER ENDED");
                    popup.hide();
                }
            }, delay);
        } else {
            jTextArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    popup.hide();
                }
            });
            jTextArea.addMouseWheelListener(e -> popup.hide());
        }
    }
}
