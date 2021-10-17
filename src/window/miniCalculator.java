package window;

import MiniCalculator.calculator;
import Phrases.Phrases;
import utilitis.CustomJButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class miniCalculator extends Overlays {

    private final calculator calculator;
    private JPanel contentPanel;
    private JTextField textField;

    private boolean calculated;

    private Dimension buttonDim;

    public miniCalculator(Point location, window.Window moneyWindow) {
        super(location, moneyWindow);
        this.setSize(contentPanel.getSize());
        this.setLocation(location);
        calculator = new calculator();
    }

    @Override
    public void build(java.awt.Window window) {
        buttonDim = new Dimension(25, 25);

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
        one.addActionListener(e -> textField.setText(textField.getText() + one.getText()));
        numPad.add(one, 0);
        CustomJButton two = new CustomJButton("2");
        two.setPreferredSize(buttonDim);
        two.addActionListener(e -> textField.setText(textField.getText() + two.getText()));
        numPad.add(two, 1);
        CustomJButton three = new CustomJButton("3");
        three.setPreferredSize(buttonDim);
        three.addActionListener(e -> textField.setText(textField.getText() + three.getText()));
        numPad.add(three, 2);
        CustomJButton addition = new CustomJButton("+");
        addition.setPreferredSize(buttonDim);
        addition.addActionListener(e -> textField.setText(textField.getText() + addition.getText()));
        numPad.add(addition, 3);
        // row 2
        CustomJButton four = new CustomJButton("4");
        four.setPreferredSize(buttonDim);
        four.addActionListener(e -> textField.setText(textField.getText() + four.getText()));
        numPad.add(four, 4);
        CustomJButton five = new CustomJButton("5");
        five.setPreferredSize(buttonDim);
        five.addActionListener(e -> textField.setText(textField.getText() + five.getText()));
        numPad.add(five, 5);
        CustomJButton six = new CustomJButton("6");
        six.setPreferredSize(buttonDim);
        six.addActionListener(e -> textField.setText(textField.getText() + six.getText()));
        numPad.add(six, 6);
        CustomJButton subtraction = new CustomJButton("-");
        subtraction.setPreferredSize(buttonDim);
        subtraction.addActionListener(e -> textField.setText(textField.getText() + subtraction.getText()));
        numPad.add(subtraction, 7);
        // row 3
        CustomJButton seven = new CustomJButton("7");
        seven.setPreferredSize(buttonDim);
        seven.addActionListener(e -> textField.setText(textField.getText() + seven.getText()));
        numPad.add(seven, 8);
        CustomJButton eight = new CustomJButton("8");
        eight.setPreferredSize(buttonDim);
        eight.addActionListener(e -> textField.setText(textField.getText() + eight.getText()));
        numPad.add(eight, 9);
        CustomJButton nine = new CustomJButton("9");
        nine.setPreferredSize(buttonDim);
        nine.addActionListener(e -> textField.setText(textField.getText() + nine.getText()));
        numPad.add(nine, 10);
        CustomJButton multiplication = new CustomJButton("*");
        multiplication.setPreferredSize(buttonDim);
        multiplication.addActionListener(e -> textField.setText(textField.getText() + multiplication.getText()));
        numPad.add(multiplication, 11);
        // row 4
        CustomJButton del = new CustomJButton("C");
        del.setPreferredSize(buttonDim);
        del.addActionListener(e -> textField.setText(""));
        numPad.add(del, 12);
        CustomJButton zero = new CustomJButton("0");
        zero.setPreferredSize(buttonDim);
        zero.addActionListener(e -> textField.setText(textField.getText() + zero.getText()));
        numPad.add(zero, 13);
        CustomJButton comma = new CustomJButton(",");
        comma.setPreferredSize(buttonDim);
        comma.addActionListener(e -> textField.setText(textField.getText() + comma.getText()));
        numPad.add(comma, 14);
        CustomJButton division = new CustomJButton("/");
        division.setPreferredSize(buttonDim);
        division.addActionListener(e -> textField.setText(textField.getText() + division.getText()));
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

    private void calc() {
        if (textField.getText().isBlank())
            return;
        calculator.setTextField(textField.getText().replaceAll(",", "."));
        calculator.calc();
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
        super.moneyWindow.setInputValue(String.valueOf(calculator.getResult()).replaceAll("\\.", ",") + Phrases.moneySymbol);
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
