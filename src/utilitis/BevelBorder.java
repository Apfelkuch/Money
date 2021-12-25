package utilitis;

import Phrases.Phrases;

import java.awt.*;

// TODO build the button Border
public class BevelBorder extends javax.swing.border.BevelBorder {

    /**
     * Raised bevel type.
     */
    public static final int RAISED = 0;
    /**
     * Lowered bevel type.
     */
    public static final int LOWERED = 1;


    public BevelBorder(int bevelType, Color color) {
        super(bevelType, color, color.darker());
    }

    @Override
    protected void paintRaisedBevel(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);

        g.setColor(getHighlightOuterColor(c));

        g.drawLine(0, 0, 0, h - 2);
        g.drawLine(1, 0, w - 2, 0);

        g.setColor(getHighlightInnerColor(c));
        g.drawLine(1, 1, 1, h - 3);
        g.drawLine(2, 1, w - 3, 1);

        g.setColor(getShadowOuterColor(c));
        g.drawLine(0, h - 1, w - 1, h - 1);
        g.drawLine(w - 1, 0, w - 1, h - 2);

        g.setColor(getShadowInnerColor(c));
        g.drawLine(1, h - 2, w - 2, h - 2);
        g.drawLine(w - 2, 1, w - 2, h - 3);

        g.translate(-x, -y);
        g.setColor(oldColor);

    }

    @Override
    protected void paintLoweredBevel(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);

        g.setColor(getShadowInnerColor(c));
        g.drawLine(0, 0, 0, h - 1);
        g.drawLine(1, 0, w - 1, 0);

        g.setColor(getShadowOuterColor(c));
        g.drawLine(1, 1, 1, h - 2);
        g.drawLine(2, 1, w - 2, 1);

        g.setColor(getHighlightOuterColor(c));
        g.drawLine(1, h - 1, w - 1, h - 1);
        g.drawLine(w - 1, 1, w - 1, h - 2);

        g.setColor(getHighlightInnerColor(c));
        g.drawLine(2, h - 2, w - 2, h - 2);
        g.drawLine(w - 2, 2, w - 2, h - 3);

        g.translate(-x, -y);
        g.setColor(oldColor);

    }

}