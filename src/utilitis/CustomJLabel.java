package utilitis;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class CustomJLabel extends JLabel {

    private Timer timer;

    public void setAnimatedIcon(ImageIcon icon, long period) {
        setAnimatedIcon(icon, period, true);
    }

    public void setAnimatedIcon(ImageIcon icon, long period, boolean constSize) {
        setAnimatedIcon(icon, period, constSize, 0);
    }

    public void setAnimatedIcon(ImageIcon icon, long period, boolean constSize, long delay) {
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = bufferedImage.createGraphics();
        g2D.drawImage(icon.getImage(), 0, 0, null);
        g2D.dispose();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            double degree = 0;
            BufferedImage rotatedImage = bufferedImage;

            @Override
            public void run() {
                degree++;
                degree = degree % 360;
                rotatedImage = RotateImage.rotate(bufferedImage, degree, constSize);

                setIcon(new ImageIcon(rotatedImage));
            }
        }, delay, period);
    }

    public void cancelAnimation() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
