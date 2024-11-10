package utilitis;

import phrases.Phrases;

import javax.swing.border.BevelBorder;
import javax.swing.*;
import java.awt.*;

public class CustomJButton extends JButton {

    private final BevelBorder raised, lower;

    public CustomJButton() {
        this(null);
    }

    public CustomJButton(String text) {
        super(text);
        super.setFocusPainted(false);
        super.setContentAreaFilled(false);
        raised = new BevelBorder(BevelBorder.RAISED, Phrases.COLOR_BUTTON, Phrases.COLOR_BUTTON.darker());
        lower = new BevelBorder(BevelBorder.LOWERED, Phrases.COLOR_BUTTON, Phrases.COLOR_BUTTON.darker());
        setBackground(Phrases.COLOR_BUTTON);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color originalColor = g.getColor();
        g.setColor(getBackground());
        if (getModel().isPressed()) {
            this.setBorder(lower);
        } else {
            this.setBorder(raised);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
        g.setColor(originalColor);
    }

}
