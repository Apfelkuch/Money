package utilitis;

import Phrases.Phrases;

import javax.swing.*;
import java.awt.*;

public class CustomJButton extends JButton {

    BevelBorder raised,lower;

    public CustomJButton() {
        this(null);
    }

    public CustomJButton(String text) {
        super(text);
        super.setFocusPainted(false);
        super.setContentAreaFilled(false);
        super.setRolloverEnabled(false);
        raised = new BevelBorder(BevelBorder.RAISED);
        lower = new BevelBorder(BevelBorder.LOWERED);
        setBackground(Phrases.COLOR_BUTTON);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            this.setBorder(lower);
            g.setColor(getBackground());
        } else {
            g.setColor(getBackground());
            this.setBorder(raised);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
