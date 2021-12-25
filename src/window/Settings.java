package window;

import Phrases.Phrases;
import utilitis.CustomJButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.Window;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class Settings extends Overlays {

    private JPanel content;
    private Color returnColor;
    private ArrayList<CustomJButton> customJButtonArrayList;

    private HashMap<Integer, Color> intColorHashMap;

    public Settings(Point location, window.Window moneyWindow) {
        super(location, moneyWindow);
        this.setSize(content.getSize());
        this.setLocation(location);
    }

    @Override
    public void build(Window window) {
        intColorHashMap = new HashMap<>();
        intColorHashMap.put(1, Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
        intColorHashMap.put(2, Phrases.COLOR_TABLE_HEAD_ROW);
        intColorHashMap.put(3, Phrases.COLOR_TABLE_SPLIT);
        intColorHashMap.put(4, Phrases.COLOR_CONTROL_PANEL_BACKGROUND);
        intColorHashMap.put(5, Phrases.COLOR_CONTROL_1);
        intColorHashMap.put(6, Phrases.COLOR_CONTROL_2);
        intColorHashMap.put(7, Phrases.COLOR_CONTROL_3);
        intColorHashMap.put(8, Phrases.COLOR_BUTTON);

        customJButtonArrayList = new ArrayList<>();

        content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
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

        settings.add(buildColorPickPanel(Phrases.colorTable, Phrases.COLOR_TABLE_CONTENT_BACKGROUND, e -> {
            if (returnColor != null) intColorHashMap.put(1, returnColor);
        }));
        settings.add(buildColorPickPanel(Phrases.colorTableHeadRow, Phrases.COLOR_TABLE_HEAD_ROW, e -> {
            if (returnColor != null) intColorHashMap.put(2, returnColor);
        }));
        settings.add(buildColorPickPanel(Phrases.colorSplit, Phrases.COLOR_TABLE_SPLIT, e -> {
            if (returnColor != null) intColorHashMap.put(3, returnColor);
        }));
        settings.add(buildColorPickPanel(Phrases.colorControl, Phrases.COLOR_CONTROL_PANEL_BACKGROUND, e -> {
            if (returnColor != null) intColorHashMap.put(4, returnColor);
        }));
        settings.add(buildColorPickPanel(Phrases.colorControl1, Phrases.COLOR_CONTROL_1, e -> {
            if (returnColor != null) intColorHashMap.put(5, returnColor);
        }));
        settings.add(buildColorPickPanel(Phrases.colorControl2, Phrases.COLOR_CONTROL_2, e -> {
            if (returnColor != null) intColorHashMap.put(6, returnColor);
        }));
        settings.add(buildColorPickPanel(Phrases.colorControl3, Phrases.COLOR_CONTROL_3, e -> {
            if (returnColor != null) intColorHashMap.put(7, returnColor);
        }));
        settings.add(buildColorPickPanel(Phrases.colorButton, Phrases.COLOR_BUTTON, e -> {
            if (returnColor != null) intColorHashMap.put(8, returnColor);
        }));

        JPanel buttons = new JPanel(new GridLayout(1, 2));
        buttons.setPreferredSize(new Dimension(content.getWidth(), 25));
        content.add(buttons, BorderLayout.SOUTH);


        CustomJButton button_use = new CustomJButton(Phrases.use);
        CustomJButton button_default = new CustomJButton(Phrases.normal);
        CustomJButton button_exit = new CustomJButton(Phrases.exit);

        // button_use
        button_use.setFont(Phrases.showFontPlain);
        button_use.addActionListener(e -> {
            Phrases.COLOR_TABLE_CONTENT_BACKGROUND = intColorHashMap.get(1);
            Phrases.COLOR_TABLE_HEAD_ROW = intColorHashMap.get(2);
            Phrases.COLOR_TABLE_SPLIT = intColorHashMap.get(3);
            Phrases.COLOR_CONTROL_PANEL_BACKGROUND = intColorHashMap.get(4);
            Phrases.COLOR_CONTROL_1 = intColorHashMap.get(5);
            Phrases.COLOR_CONTROL_2 = intColorHashMap.get(6);
            Phrases.COLOR_CONTROL_3 = intColorHashMap.get(7);
            Phrases.COLOR_BUTTON = intColorHashMap.get(8);
            moneyWindow.reload();
            moneyWindow.getMoney().updateAllEntries();

            // update visuals of the settings
            content.setBackground(Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
            for (CustomJButton button : customJButtonArrayList) {
//                button.setBackground(Phrases.COLOR_BUTTON);
                button.setBorderColor(Phrases.COLOR_BUTTON);
            }
            button_use.setBackground(Phrases.COLOR_BUTTON);
            button_exit.setBackground(Phrases.COLOR_BUTTON);
            button_default.setBackground(Phrases.COLOR_BUTTON);
            this.revalidate();
            this.repaint();

        });
        buttons.add(button_use);
        customJButtonArrayList.add(button_use);

        // button_default
        button_default.setFont(Phrases.showFontPlain);
        button_default.addActionListener(e -> {
            Phrases.setDefaultColors();
            intColorHashMap.put(1, Phrases.COLOR_TABLE_CONTENT_BACKGROUND);
            intColorHashMap.put(2, Phrases.COLOR_TABLE_HEAD_ROW);
            intColorHashMap.put(3, Phrases.COLOR_TABLE_SPLIT);
            intColorHashMap.put(4, Phrases.COLOR_CONTROL_PANEL_BACKGROUND);
            intColorHashMap.put(5, Phrases.COLOR_CONTROL_1);
            intColorHashMap.put(6, Phrases.COLOR_CONTROL_2);
            intColorHashMap.put(7, Phrases.COLOR_CONTROL_3);
            intColorHashMap.put(8, Phrases.COLOR_BUTTON);
            for (int i = 0; i < intColorHashMap.size(); i++) {
                customJButtonArrayList.get(i).setBackground(intColorHashMap.get(i+1));
            }
        });
        buttons.add(button_default);
        customJButtonArrayList.add(button_default);

        // button_exit
        button_exit.setFont(Phrases.showFontPlain);
        button_exit.addActionListener(e -> {
            moneyWindow.setSettings(null);
            this.dispose();
        });
        buttons.add(button_exit);
        customJButtonArrayList.add(button_exit);
    }

    private JPanel buildColorPickPanel(String text, Color defaultColor, ActionListener actionListener) {
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JLabel label_text = new JLabel(text);
        label_text.setPreferredSize(new Dimension(250, 25));
        label_text.setFont(Phrases.showFontPlain);
        jPanel.add(label_text);

        CustomJButton customJButton = new CustomJButton();
        customJButton.setBackground(defaultColor);
        customJButton.setPreferredSize(new Dimension(50, 25));
        customJButton.addActionListener(actionListener); // is processed second
        customJButton.addActionListener(e -> { // is processed first
            returnColor = JColorChooser.showDialog(this, customJButton.getText(), customJButton.getBackground());
            if (returnColor == null) {
                return;
            }
            customJButton.setBackground(returnColor);
        });
        jPanel.add(customJButton);
        customJButtonArrayList.add(customJButton);

        return jPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
