package window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

// TODO build the date choosing overlay
public class choseDate extends Overlays {

    private JTextField textField;
    private JPanel jPanel;

    public choseDate(Point location, window.Window moneyWindow) {
        super(location, moneyWindow);
        this.setSize(jPanel.getSize());
        this.setLocation(location);
    }

    @Override
    public void build(java.awt.Window window) {
        // build GUI
        jPanel = new JPanel();
        jPanel.setSize(100, 100);
        jPanel.setLayout(null);
        this.add(jPanel);

        JButton close = new JButton("close");
        close.setSize(new Dimension(100, 25));
        close.addActionListener(e -> {
            this.dispose();
            super.moneyWindow.setChoseDate(null);
        });
        close.setLocation(0, 0);
        close.requestFocus();
        close.requestFocusInWindow();
        jPanel.add(close);

        JButton button = new JButton("Test");
        button.setSize(close.getSize());
        button.setLocation(0, 25);
        jPanel.add(button);

        textField = new JTextField();
        textField.setSize(new Dimension(100, 25));
        textField.setLocation(0, 50);
        jPanel.add(textField);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_DELETE || e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
        } else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            this.dispose();
            super.moneyWindow.setChoseDate(null);
        } else if (Character.isDefined(e.getKeyChar())) {
            textField.setText(textField.getText() + e.getKeyChar());
        }
    }

    @Override
    public void setLocation(Point p) {
        this.setLocation(
                p.x - jPanel.getWidth() + Window.extraButton.width,
                p.y - jPanel.getHeight() + Window.extraButton.height
        );
    }
}
