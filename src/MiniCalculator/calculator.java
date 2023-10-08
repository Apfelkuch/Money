package MiniCalculator;

import Phrases.Phrases;

import java.util.ArrayList;

public class calculator {

    public final char[] validChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '+', '-', '*', '/'};
    private final char[] operatorChars = {'+', '-', '*', '/'};
    private String content;

    private double result = 0.0d;
    private int decimalPlaces = 2;

    // return Values
    public final int calculation_successful = 0;
    public final int calculation_fails = 1;
    public final int calculation_successful_with_rounding = 2;

    /**
     * Calculate the expression and return the result.
     * If the result is negative the 0 is returned.
     *
     * @throws IllegalArgumentException Throw if two or more operators are following each other directly.
     */
    public int calc() throws IllegalArgumentException {
        // make sure that no invalid character are in the stringProperty
        for (int i = 0; i < content.length(); i++) {
            char charAt = content.charAt(i);
            if (Character.isLetter(charAt)) {
                System.err.println("[Error] calculator.calc >> character is not valid. Letter at : " + charAt);
                content = "";
                return calculation_fails;
            }
            boolean containInvalidChar = true;
            for (Character c : validChars) {
                if (charAt == c) {
                    containInvalidChar = false;
                    break;
                }
            }
            if (containInvalidChar) {
                System.err.println("[Error] calculator.calc >> character is not valid");
                content = "";
                return calculation_fails;
            }
        }
        // check if the content ends with an operator
        if (content.charAt(content.length() - 1) == '+' ||
                content.charAt(content.length() - 1) == '-' ||
                content.charAt(content.length() - 1) == '*' ||
                content.charAt(content.length() - 1) == '/') {
            content = "";
            return calculation_fails;
        }
        // return if the text is empty or if the first character is not a digit, but a comma as first is possible
        if (content.isBlank() || (!Character.isDigit(content.charAt(0)) && !(content.charAt(0) == '.'))) {
            content = "";
            return calculation_fails;
        }

        // find the operators
        ArrayList<IntChar> operators = new ArrayList<>();
        for (int i = 0; i < content.length(); i++) {
            for (char c : operatorChars) {
                if (c == content.charAt(i)) {
                    operators.add(new IntChar(i, content.charAt(i)));
                    break;
                }
            }
        }
        if (operators.size() == 0) {
            return calculation_fails;
        }
//        System.out.println("MiniCalculator.calc.operators >> " + Arrays.toString(operators.toArray(new IntChar[0])));

        // divide the text in different numbers between the operators
        double[] numbers = new double[operators.size() + 1];
        int place = 0;
        String part = "";
        try {
            for (int i = 0; i < content.length(); i++) {
                if (content.charAt(i) == '+' || content.charAt(i) == '-' || content.charAt(i) == '*' || content.charAt(i) == '/') {
                    numbers[place] = Double.parseDouble(part);
                    part = "";
                    place++;
                } else {
                    part += content.charAt(i);
                }
            }
            numbers[place] = Double.parseDouble(part);
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Syntax error in calculation expression");
            throw new IllegalArgumentException("Syntax error in calculation expression");
        }
//        System.out.println("MiniCalculator.calc.numbers >> " + Arrays.toString(numbers));

        // cut the decimal places to down to max decimal place number (decimalPlaces)
        for (int i = 0; i < numbers.length; i++) {
            String doubleString = Double.valueOf(numbers[i]).toString();
            int pos = doubleString.indexOf(".") + 1;
            if (doubleString.substring(pos).length() > decimalPlaces) {
                numbers[i] = Double.parseDouble(doubleString.substring(0, pos + decimalPlaces));
            }
        }
//        System.out.println("MiniCalculator.calc.numbers >> " + Arrays.toString(numbers));

        int returnValue = calculation_successful;
        // calculate the result
        double emptyMarker = Phrases.calculatorMinValue - 1;
        // calculate multiplication and division from left to right
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).character == '*') { // multiplication
                double result = numbers[i] * numbers[i + 1];
                numbers[i] = result;
                numbers[i + 1] = emptyMarker; // mark the place as empty
                operators.remove(i);
                i--;
                numbers = trimArray(numbers, emptyMarker);
                this.result = result;
            } else if (operators.get(i).character == '/') { // division
                double result = numbers[i] / numbers[i + 1];
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
                double result = numbers[i] + numbers[i + 1];
                numbers[i] = result;
                numbers[i + 1] = emptyMarker; // mark the place as empty
                operators.remove(i);
                i--;
                numbers = trimArray(numbers, emptyMarker);
                this.result = result;
            } else if (operators.get(i).character == '-') { // subtraction
                double result = numbers[i] - numbers[i + 1];
                numbers[i] = result;
                numbers[i + 1] = emptyMarker;
                operators.remove(i);
                i--;
                numbers = trimArray(numbers, emptyMarker);
                this.result = result;
            }
        }

        // adjust the result. If the result is smaller than the minimum achievable value, the result is set to the minimum achievable value. Turn negative results into zero's.
        if (result < Phrases.calculatorMinValue) {
            returnValue = calculation_successful_with_rounding;
            result = 0;
        }
        // adjust the result. If the result is higher than the maximum achievable value, the result is set to the maximum achievable value.
        if (result > Phrases.calculatorMaxValue) {
            returnValue = calculation_successful_with_rounding;
            result = Phrases.calculatorMaxValue;
        }

        // trim the result on two decimal places
        String s = String.valueOf(result);
        int end = s.indexOf('.') + 1 + decimalPlaces;
        end = end > s.length() ? s.length() : end;
        this.content = s.substring(0, end);

        return returnValue;
    }

    public double getResult() throws NullPointerException {
        if (content.equals("")) {
            return Double.parseDouble("0");
        }
        return Double.parseDouble(content);
    }

    private double[] trimArray(double[] array, double emptyMarker) {
        // find he number of emptyMarkers in the array
        int countEmptyMarkers = 0;
        for (double element : array) {
            if (element == emptyMarker) {
                countEmptyMarkers++;
            }
        }
        // move the emptyMarkers to the end
        for (int j = 0; j < countEmptyMarkers; j++) {
            for (int i = 1; i < array.length; i++) {
                if (array[i - 1] == emptyMarker) {
                    double l = array[i - 1];
                    array[i - 1] = array[i];
                    array[i] = l;
                }
            }
        }
        double[] result = new double[array.length - countEmptyMarkers];
        System.arraycopy(array, 0, result, 0, result.length);
        return result;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public void setTextField(String s) {
        content = s;
    }

}
