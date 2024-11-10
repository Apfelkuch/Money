package window;

import minicalculator.Calculator;
import phrases.Phrases;
import utilitis.CustomJButton;
import utilitis.CustomPopup;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MiniCalculator extends Overlays {

    private final Calculator calculator;
    private JPanel contentPanel;
    private JTextField textField;

    private boolean calculated;

    public MiniCalculator(Point location, window.Window moneyWindow) {
        super(location, moneyWindow);
        this.setSize(contentPanel.getSize());
        this.setLocation(location);
        calculator = new Calculator();
    }

    @Override
    public void build(java.awt.Window window) {
        Dimension buttonDim = new Dimension(25, 25);

        contentPanel = new JPanel();
        contentPanel.setSize(5 * buttonDim.width, 7 * buttonDim.height);
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentPanel.setBorder(new LineBorder(Color.BLACK, 2));
        this.add(contentPanel);

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(4 * buttonDim.width, buttonDim.height));
        textField.setBorder(new LineBorder(Color.BLACK, 1));
        contentPanel.add(textField);

        JPanel numPad = new JPanel();
        numPad.setPreferredSize(new Dimension(4 * buttonDim.width, 5 * buttonDim.height));
        numPad.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        // row 1
        CustomJButton one = new CustomJButton("1");
        one.setPreferredSize(buttonDim);
        one.addActionListener(e -> ActionListener(one));
        numPad.add(one, 0);
        CustomJButton two = new CustomJButton("2");
        two.setPreferredSize(buttonDim);
        two.addActionListener(e -> ActionListener(two));
        numPad.add(two, 1);
        CustomJButton three = new CustomJButton("3");
        three.setPreferredSize(buttonDim);
        three.addActionListener(e -> ActionListener(three));
        numPad.add(three, 2);
        CustomJButton addition = new CustomJButton("+");
        addition.setPreferredSize(buttonDim);
        addition.addActionListener(e -> ActionListener(addition));
        numPad.add(addition, 3);
        // row 2
        CustomJButton four = new CustomJButton("4");
        four.setPreferredSize(buttonDim);
        four.addActionListener(e -> ActionListener(four));
        numPad.add(four, 4);
        CustomJButton five = new CustomJButton("5");
        five.setPreferredSize(buttonDim);
        five.addActionListener(e -> ActionListener(five));
        numPad.add(five, 5);
        CustomJButton six = new CustomJButton("6");
        six.setPreferredSize(buttonDim);
        six.addActionListener(e -> ActionListener(six));
        numPad.add(six, 6);
        CustomJButton subtraction = new CustomJButton("-");
        subtraction.setPreferredSize(buttonDim);
        subtraction.addActionListener(e -> ActionListener(subtraction));
        numPad.add(subtraction, 7);
        // row 3
        CustomJButton seven = new CustomJButton("7");
        seven.setPreferredSize(buttonDim);
        seven.addActionListener(e -> ActionListener(seven));
        numPad.add(seven, 8);
        CustomJButton eight = new CustomJButton("8");
        eight.setPreferredSize(buttonDim);
        eight.addActionListener(e -> ActionListener(eight));
        numPad.add(eight, 9);
        CustomJButton nine = new CustomJButton("9");
        nine.setPreferredSize(buttonDim);
        nine.addActionListener(e -> ActionListener(nine));
        numPad.add(nine, 10);
        CustomJButton multiplication = new CustomJButton("*");
        multiplication.setPreferredSize(buttonDim);
        multiplication.addActionListener(e -> ActionListener(multiplication));
        numPad.add(multiplication, 11);
        // row 4
        CustomJButton del = new CustomJButton("C");
        del.setPreferredSize(buttonDim);
        del.addActionListener(e -> {
            textField.setText("");
            calculated = false;
        });
        numPad.add(del, 12);
        CustomJButton zero = new CustomJButton("0");
        zero.setPreferredSize(buttonDim);
        zero.addActionListener(e -> ActionListener(zero));
        numPad.add(zero, 13);
        CustomJButton comma = new CustomJButton(",");
        comma.setPreferredSize(buttonDim);
        comma.addActionListener(e -> ActionListener(comma));
        numPad.add(comma, 14);
        CustomJButton division = new CustomJButton("/");
        division.setPreferredSize(buttonDim);
        division.addActionListener(e -> ActionListener(division));
        numPad.add(division, 15);
        // row 5
        CustomJButton back = new CustomJButton("back");
        back.setPreferredSize(new Dimension(2 * buttonDim.width, buttonDim.height));
        back.addActionListener(e -> {
            this.dispose();
            super.moneyWindow.setMiniCalculator(null);
        });
        numPad.add(back, 16);
        CustomJButton use = new CustomJButton("use");
        use.setPreferredSize(new Dimension(2 * buttonDim.width, buttonDim.height));
        use.addActionListener(e -> {
            if (calculated) {
                use();
            } else {
                calc();
            }
        });
        numPad.add(use, 17);

        contentPanel.add(numPad);

    }

    private void ActionListener(CustomJButton customJButton) {
        textField.setText(textField.getText() + customJButton.getText());
        calculated = false;
    }

    private void calc() {
        if (textField.getText().isBlank())
            return;
        calculator.setTextField(textField.getText().replaceAll(",", "."));
        int returnValue;
        try {
            returnValue = calculator.calc();
        } catch (IllegalArgumentException e) {
            new CustomPopup(textField.getLocationOnScreen().x, textField.getLocationOnScreen().y + textField.getHeight(), Phrases.syntaxErrorInCalculationExpression);
            return;
        }
        if (returnValue == calculator.calculation_successful_with_rounding) {
            new CustomPopup(textField.getLocationOnScreen().x, textField.getLocationOnScreen().y + textField.getHeight(), Phrases.calculationSuccessfulWithRounding);
        }
        textField.setText(calculator.getResult() + "");
        calculated = true;
    }

    private void use() {
        if (!calculated) {
            return;
        }
        if (textField.getText().isBlank()) {
            return;
        }
        super.moneyWindow.setInputValue(String.valueOf(calculator.getResult()));
        this.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && textField.getText().length() > 0) {
            textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
            calculated = false;
        } else if (e.getKeyChar() == KeyEvent.VK_DELETE) {
            textField.setText("");
            calculated = false;
        } else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            this.dispose();
            super.moneyWindow.setMiniCalculator(null);
        } else if (e.getKeyChar() == KeyEvent.VK_ENTER && !calculated) {
            this.calc();
        } else if (e.getKeyChar() == KeyEvent.VK_ENTER && calculated) {
            this.use();
        } else if (!Character.isAlphabetic(e.getKeyChar()) && !(e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
            calculated = false;
            for (Character c : calculator.validChars) {
                if (e.getKeyCode() == c.hashCode()) {
                    return;
                }
            }
            textField.setText(textField.getText() + e.getKeyChar());
        }
    }

    @Override
    public void setLocation(Point p) {
        if (contentPanel == null) {
            return;
        }
        this.setLocation(
                p.x - contentPanel.getWidth() + Window.extraButton.width,
                p.y - contentPanel.getHeight() + Window.extraButton.height
        );
    }
}
