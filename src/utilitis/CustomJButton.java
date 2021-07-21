package utilitis;

import javax.swing.*;
import java.awt.*;

public class CustomJButton extends JButton {

    public CustomJButton() {
        this(null);
    }

    public CustomJButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
        super.setRolloverEnabled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            this.setBorder(BorderFactory.createLoweredBevelBorder());
            g.setColor(getBackground());
        } else {
            g.setColor(getBackground());
            this.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
