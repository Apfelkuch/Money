package MiniCalculator;

import Phrases.Phrases;
import utilitis.CustomJButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MiniCalculator {

    private final char[] chars = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, '+', '-', '*', '/'};

    private final JTextField textField;
    private final JPopupMenu popupMenu;
    private boolean running;

    private BigDecimal result;

    public MiniCalculator(Point location, JFrame parent) {
        Dimension dim = new Dimension(25,25);

        popupMenu = new JPopupMenu();
        popupMenu.setPopupSize(5 * dim.width, 6 * dim.height);
        popupMenu.setPreferredSize(new Dimension(5 * dim.width, 6 * dim.height));
        if (location == null) {
            if (parent == null) { // location == null && parent == null -> center of the screen
                popupMenu.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - popupMenu.getPreferredSize().width / 2,
                        Toolkit.getDefaultToolkit().getScreenSize().height / 2 - popupMenu.getPreferredSize().height / 2);
            } else { // location == null && parent != null -> center of the parent
                popupMenu.setLocation(parent.getLocationOnScreen().x + parent.getWidth() / 2 - popupMenu.getPreferredSize().width / 2,
                        parent.getLocationOnScreen().y + parent.getHeight() / 2 - popupMenu.getPreferredSize().height / 2);
            }
        } else { // location != null -> use the given location
            popupMenu.setLocation(location);
        }
        popupMenu.setLayout(new FlowLayout(FlowLayout.CENTER));
        popupMenu.setBorder(new LineBorder(Color.BLACK, 2));
        popupMenu.setBackground(Color.BLUE);
        popupMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                close();
            }
        });

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(4 * dim.width, dim.height));
        textField.setBorder(new LineBorder(Color.BLACK, 1));
        popupMenu.add(textField);

        JPanel numPad = new JPanel();
        numPad.setPreferredSize(new Dimension(4 * dim.width, 4 * dim.height));
        numPad.setLayout(new GridLayout(4, 4));

        // row 1
        CustomJButton one = new CustomJButton("1");
        one.setPreferredSize(dim);
        one.addActionListener(e -> textField.setText(textField.getText() + one.getText()));
//        one.setFocusable(false);
        numPad.add(one, 0);
        CustomJButton two = new CustomJButton("2");
        two.setPreferredSize(dim);
        two.addActionListener(e -> textField.setText(textField.getText() + two.getText()));
//        two.setFocusable(false);
        numPad.add(two, 1);
        CustomJButton three = new CustomJButton("3");
        three.setPreferredSize(dim);
        three.addActionListener(e -> textField.setText(textField.getText() + three.getText()));
//        three.setFocusable(false);
        numPad.add(three, 2);
        CustomJButton addition = new CustomJButton("+");
        addition.setPreferredSize(dim);
        addition.addActionListener(e -> textField.setText(textField.getText() + addition.getText()));
//        addition.setFocusable(false);
        numPad.add(addition, 3);
        // row 2
        CustomJButton four = new CustomJButton("4");
        four.setPreferredSize(dim);
        four.addActionListener(e -> textField.setText(textField.getText() + four.getText()));
//        four.setFocusable(false);
        numPad.add(four, 4);
        CustomJButton five = new CustomJButton("5");
        five.setPreferredSize(dim);
        five.addActionListener(e -> textField.setText(textField.getText() + five.getText()));
//        five.setFocusable(false);
        numPad.add(five, 5);
        CustomJButton six = new CustomJButton("6");
        six.setPreferredSize(dim);
        six.addActionListener(e -> textField.setText(textField.getText() + six.getText()));
//        six.setFocusable(false);
        numPad.add(six, 6);
        CustomJButton subtraction = new CustomJButton("-");
        subtraction.setPreferredSize(dim);
        subtraction.addActionListener(e -> textField.setText(textField.getText() + subtraction.getText()));
//        subtraction.setFocusable(false);
        numPad.add(subtraction, 7);
        // row 3
        CustomJButton seven = new CustomJButton("7");
        seven.setPreferredSize(dim);
        seven.addActionListener(e -> textField.setText(textField.getText() + seven.getText()));
//        seven.setFocusable(false);
        numPad.add(seven, 8);
        CustomJButton eight = new CustomJButton("8");
        eight.setPreferredSize(dim);
        eight.addActionListener(e -> textField.setText(textField.getText() + eight.getText()));
//        eight.setFocusable(false);
        numPad.add(eight, 9);
        CustomJButton nine = new CustomJButton("9");
        nine.setPreferredSize(dim);
        nine.addActionListener(e -> textField.setText(textField.getText() + nine.getText()));
//        nine.setFocusable(false);
        numPad.add(nine, 10);
        CustomJButton multiplication = new CustomJButton("*");
        multiplication.setPreferredSize(dim);
        multiplication.addActionListener(e -> textField.setText(textField.getText() + multiplication.getText()));
//        multiplication.setFocusable(false);
        numPad.add(multiplication, 11);
        // row 4
        CustomJButton del = new CustomJButton("del");
        del.setPreferredSize(dim);
        del.addActionListener(e -> textField.setText(""));
//        del.setFocusable(false);
        numPad.add(del, 12);
        CustomJButton zero = new CustomJButton("0");
        zero.setPreferredSize(dim);
        zero.addActionListener(e -> textField.setText(textField.getText() + zero.getText()));
//        zero.setFocusable(false);
        numPad.add(zero, 13);
        CustomJButton calc = new CustomJButton("=");
        calc.setPreferredSize(dim);
        calc.addActionListener(e -> calc());
//        calc.setFocusable(false);
        numPad.add(calc, 14);
        CustomJButton division = new CustomJButton("/");
        division.setPreferredSize(dim);
        division.addActionListener(e -> textField.setText(textField.getText() + division.getText()));
//        division.setFocusable(false);
        numPad.add(division, 15);

        popupMenu.add(numPad);
        popupMenu.pack();

        popupMenu.setVisible(true);

        if (parent != null) { // show the popup only when the parent is active
            running = true;
            boolean prevState = false;
            while (running) {
                if (parent.isActive() != prevState) {
                    System.out.println("MiniCalculator.MiniCalculator: functional");
                    prevState = parent.isActive();
                    popupMenu.setVisible(prevState);
                }
            }
        }
    }

    private void close() {
        running = false;
        popupMenu.setVisible(false);
    }

    private void calc() { // TODO calculate the input
        String text = textField.getText();
        if (text.isBlank()) return; // return if the text is empty
        if (!Character.isDigit(text.charAt(0))) return; // return if the first character is not a digit

        // find the operators
        ArrayList<IntChar> operators = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                operators.add(new IntChar(i, text.charAt(i)));
            }
        }
        System.out.println("MiniCalculator.calc.operators >> " + Arrays.toString(operators.toArray(new IntChar[0])));

        // divide the text in different numbers between the operators
        BigDecimal[] numbers = new BigDecimal[operators.size() + 1];
        int place = 0;
        String part = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '+' || text.charAt(i) == '-' || text.charAt(i) == '*' || text.charAt(i) == '/') {
                numbers[place] = BigDecimal.valueOf(Double.parseDouble(part));
                part = "";
                place++;
            } else {
                part += text.charAt(i);
            }
        }
        numbers[place] = BigDecimal.valueOf(Double.parseDouble(part));
        System.out.println("MiniCalculator.calc.numbers >> " + Arrays.toString(numbers));

        // calculate the result
        BigDecimal emptyMarker = new BigDecimal(Phrases.inputValueMin - 1);
        MathContext mathContext = new MathContext(2, RoundingMode.DOWN);
        // TODO not sure if from left to right or from right to left
        // calculate multiplication and division from left to right
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).character == '*') { // multiplication
                BigDecimal result = numbers[i].multiply(numbers[i + 1], mathContext);
                numbers[i] = result;
                numbers[i + 1] = emptyMarker; // mark the place as empty
                operators.remove(i);
                i--;
                numbers = trimArray(numbers, emptyMarker);
                this.result = result;
            } else if (operators.get(i).character == '/') { // division
                BigDecimal result = numbers[i].divide(numbers[i + 1], mathContext);
                numbers[i] = result;
                numbers[i + 1] = emptyMarker;
                operators.remove(i);
                i--;
                numbers = trimArray(numbers, emptyMarker);
                this.result = result;
            }
        }

        // calculate addition and subtraction from left to right
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).character == '+') { // addition
                BigDecimal result = numbers[i].add(numbers[i + 1], mathContext);
                System.out.println("MiniCalculator.calc >> result: " + result);
                numbers[i] = result;
                numbers[i + 1] = emptyMarker; // mark the place as empty
                operators.remove(i);
                i--;
                numbers = trimArray(numbers, emptyMarker);
                this.result = result;
            } else if (operators.get(i).character == '-') { // subtraction
                BigDecimal result = numbers[i].subtract(numbers[i + 1], mathContext);
                numbers[i] = result;
                numbers[i + 1] = emptyMarker;
                operators.remove(i);
                i--;
                numbers = trimArray(numbers, emptyMarker);
                this.result = result;
            }
        }

        // show the result
        try {
            textField.setText(result + "");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private BigDecimal[] trimArray(BigDecimal[] array, BigDecimal emptyMarker) {
        // find he number of emptyMarkers in the array
        int countEmptyMarkers = 0;
        for (BigDecimal bigDecimal : array) {
            if (bigDecimal.equals(emptyMarker)) {
                countEmptyMarkers++;
            }
        }
        // move the emptyMarkers to the end
        for (int j = 0; j < countEmptyMarkers; j++) {
            for (int i = 1; i < array.length; i++) {
                if (array[i - 1].equals(emptyMarker)) {
                    BigDecimal l = array[i - 1];
                    array[i - 1] = array[i];
                    array[i] = l;
                }
            }
        }
        BigDecimal[] result = new BigDecimal[array.length - countEmptyMarkers];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public void keyTyped(KeyEvent e) {
        System.out.println("MiniCalculator.keyTyped >> e: " + e);
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("MiniCalculator.keyPressed >> e: " + e);
        if (Character.isAlphabetic(e.getKeyChar())) {
            System.out.println("MiniCalculator.keyPressed >> e.isAlpha: " + e.getKeyChar());
            return;
        }
        for (Character c : chars) {
            if (e.getKeyCode() == c.hashCode()) {
                System.out.println("MiniCalculator.keyPressed >> e.isInvalid: " + e.getKeyChar());
                return;
            }
        }
        textField.setText(textField.getText() + e.getKeyChar());
    }

    public void keyReleased(KeyEvent e) {
        System.out.println("MiniCalculator.keyReleased >> e: " + e);
    }

    public static void main(String[] args) {
//        // testing trimArray Methode : successful
//        int[] a = {2, 3, 5, 6, 7, 5, -2, -2, 3, 2, 2};
//        System.out.println(Arrays.toString(a));
//        a = new MiniCalculator().trimArray(a, -2);
//        System.out.println(Arrays.toString(a));

        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocation(200, 200);
        frame.setLayout(new FlowLayout());

        frame.setVisible(true);

        // TODO KeyListener
        MiniCalculator miniCalculator = new MiniCalculator(null, frame);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("MiniCalculator.keyTyped >> e: " + e);
                miniCalculator.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("MiniCalculator.keyPressed >> e: " + e);
                miniCalculator.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("MiniCalculator.keyReleased >> e: " + e);
                miniCalculator.keyReleased(e);
            }
        });

        JButton button = new CustomJButton("show");
        button.addActionListener(e -> new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    MiniCalculator miniCalculator = new MiniCalculator(null, frame);

                }
            }, 1)
        );
        frame.add(button);

        frame.setVisible(true);
    }

}
