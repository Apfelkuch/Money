package utilitis;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class CustomPopup {
    public CustomPopup(int x, int y, String message) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new JLabel(message));
        popupMenu.setLocation(x, y);
        popupMenu.setVisible(true);
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                popupMenu.setVisible(false);
            }
        }, 2000);
    }
}
