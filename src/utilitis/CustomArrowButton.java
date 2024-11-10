package utilitis;

import phrases.Phrases;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;

public class CustomArrowButton extends BasicArrowButton {

    BevelBorder raised;
    BevelBorder lower;

    public CustomArrowButton(int direction) {
        super(direction, null, null, Phrases.COLOR_BUTTON.darker().darker(), null);
        super.setBorder(BorderFactory.createRaisedBevelBorder());
        raised = new BevelBorder(BevelBorder.RAISED, Phrases.COLOR_BUTTON);
        lower = new BevelBorder(BevelBorder.LOWERED, Phrases.COLOR_BUTTON);
        setBackground(Phrases.COLOR_BUTTON);
    }

    @Override
    public void paint(Graphics g) {
        Color origColor;
        boolean isPressed, isEnabled;
        int w, h, size;

        w = getSize().width;
        h = getSize().height;
        origColor = g.getColor();
        isPressed = getModel().isPressed();
        isEnabled = isEnabled();

        g.setColor(getBackground());
        g.fillRect(0, 0, h, w);

        // Draw the proper Border
        if (isPressed) {
            this.setBorder(lower);
        } else {
            this.setBorder(raised);
        }
        this.paintBorder(g);

        // If there is no room to draw arrow, return
        if (h < 5 || w < 5) {
            g.setColor(origColor);
            return;
        }

        if (isPressed) {
            g.translate(1,1);
        } else {
            g.translate(1,0);
        }

        // Draw the arrow
        size = Math.min((h - 4) / 3, (w - 4) / 3);
        size = Math.max(size, 2);
        paintTriangle(g, (w - size) / 2, (h - size) / 2,
                size, direction, isEnabled);

        // Reset the Graphics translation and color
        if (isPressed) {
            g.translate(-1,-1);
        } else {
            g.translate(-1,0);
        }
        g.setColor(origColor);
    }
}
