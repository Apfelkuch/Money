package MiniCalculator;

import Phrases.Phrases;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class calculator {

    private final char[] operatorChars = {'+', '-', '*', '/'};
    public final char[] validChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '+', '-', '*', '/'};

    private String content;

    private BigDecimal result;

    public void calc() {
        // make sure that no invalid character are in the stringProperty
        for (int i = 0; i < content.length(); i++) {
            char charAt = content.charAt(i);
            if (Character.isLetter(charAt)) {
                System.out.println("MiniCalculator.keyPressed >> character.isAlpha: " + charAt);
                content = "";
                return;
            }
            boolean containInvalidChar = true;
            for (Character c : validChars) {
                if (charAt == c) {
                    containInvalidChar = false;
                    break;
                }
            }
            if (containInvalidChar) {
                content = "";
                return;
            }
        }
        if (content.charAt(content.length() - 1) == '+' ||
                content.charAt(content.length() - 1) == '-' ||
                content.charAt(content.length() - 1) == '*' ||
                content.charAt(content.length() - 1) == '/') {
            content = "";
            return;
        }

        String text = content;
        if (text.isBlank()) // return if the text is empty
            return;
        if (!Character.isDigit(text.charAt(0))) // return if the first character is not a digit
            return;

        // find the operators
        ArrayList<IntChar> operators = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            for (char c : operatorChars) {
                if (c == text.charAt(i)) {
                    operators.add(new IntChar(i, text.charAt(i)));
                    break;
                }
            }
        }
//        System.out.println("MiniCalculator.calc.operators >> " + Arrays.toString(operators.toArray(new IntChar[0])));

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
//        System.out.println("MiniCalculator.calc.numbers >> " + Arrays.toString(numbers));

        // calculate the result
        BigDecimal emptyMarker = new BigDecimal(Phrases.inputValueMin - 1);
        MathContext mathContext = new MathContext(2, RoundingMode.DOWN);
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
            content = result + "";
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public float getResult() throws NullPointerException {
        if (content.equals("")) {
            return Float.parseFloat("0");
        }
        return Float.parseFloat(result.toString());
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
        System.arraycopy(array, 0, result, 0, result.length);
        return result;
    }

    public void setTextField(String s) {
        content = s;
    }

}
