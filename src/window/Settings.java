package window;

import Phrases.Phrases;
import utilitis.CustomJButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.Window;
import java.awt.*;
import java.awt.event.KeyEvent;

// TODO not finished and not sure if needed
public class Settings extends Overlays {

    private JPanel content;

    public Settings(Point location, window.Window moneyWindow) {
        super(location, moneyWindow);
        this.setSize(content.getSize());
        this.setLocation(location);
    }

    @Override
    public void build(Window window) {
        content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setSize(320, 25 * 12);
        content.setBorder(new LineBorder(Color.BLACK, 2));
        this.add(content);

        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        content.add(north, BorderLayout.NORTH);

        JLabel label = new JLabel(Phrases.settings);
        label.setPreferredSize(new Dimension(200, 25));
        label.setFont(Phrases.showFontBold);
        north.add(label);

        JPanel settings = new JPanel();
        settings.setLayout(new FlowLayout(FlowLayout.LEFT));
        content.add(settings, BorderLayout.CENTER);

        settings.add(buildColorPickPanel(Phrases.colorTable, Phrases.COLOR_TABLE_BACKGROUND));
        settings.add(buildColorPickPanel(Phrases.colorTableHeadRow, Phrases.COLOR_TABLE_HEAD_ROW));
        settings.add(buildColorPickPanel(Phrases.colorSplit, Phrases.COLOR_TABLE_SPLIT));
        settings.add(buildColorPickPanel(Phrases.colorControl, Phrases.COLOR_CONTROL_PANEL_BACKGROUND));
        settings.add(buildColorPickPanel(Phrases.colorControl1, Phrases.COLOR_CONTROL_1));
        settings.add(buildColorPickPanel(Phrases.colorControl2, Phrases.COLOR_CONTROL_2));
        settings.add(buildColorPickPanel(Phrases.colorControl3, Phrases.COLOR_CONTROL_3));
        settings.add(buildColorPickPanel(Phrases.colorButton, Phrases.COLOR_BUTTON));

        CustomJButton exit = new CustomJButton(Phrases.exit);
        exit.setPreferredSize(new Dimension(50, 25));
        exit.setFont(Phrases.showFontPlain);
        exit.addActionListener(e -> {
            moneyWindow.setSettings(null);
            this.dispose();
        });

        content.add(exit, BorderLayout.SOUTH);
    }

    private JPanel buildColorPickPanel(String text, Color defaultColor) {
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JLabel label_text = new JLabel(text);
        label_text.setPreferredSize(new Dimension(250, 25));
        label_text.setFont(Phrases.showFontPlain);
        jPanel.add(label_text);

        CustomJButton customJButton = new CustomJButton();
        customJButton.setBackground(defaultColor);
        customJButton.setPreferredSize(new Dimension(50, 25));
        customJButton.addActionListener(e -> {
            Color returnColor = JColorChooser.showDialog(this, customJButton.getText(), customJButton.getBackground());
            customJButton.setBackground(returnColor);
            // TODO adjust the color in the program with the new color
        });
        jPanel.add(customJButton);

        return jPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
