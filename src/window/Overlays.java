package window;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class Overlays extends java.awt.Window {

    protected window.Window moneyWindow;

    public Overlays(Point location, window.Window moneyWindow) {
        // window setup
        super(moneyWindow);
        this.moneyWindow = moneyWindow;

        this.setSize(100,100);
        this.setLocation(location);
        this.setVisible(true);

        build(this);
    }

    public abstract void build(java.awt.Window window);

    public abstract void keyTyped(KeyEvent e);

    @Override
    public void setLocation(Point p) {
        if (p == null) {
            p = new Point(
                    moneyWindow.getLocationOnScreen().x + moneyWindow.getWidth() / 2 - this.getWidth() / 2,
                    moneyWindow.getLocationOnScreen().y + moneyWindow.getHeight() / 2 - this.getHeight() / 2);
        }
        super.setLocation(p);
    }
}
