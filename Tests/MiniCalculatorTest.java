import MiniCalculator.calculator;

import java.util.Arrays;
public class MiniCalculatorTest {

    public static void main(String[] args) {
        // The task must be written without spaces between numbers and symbols.
        // The decimal place in numbers must be a point, not a comma.
        String[] values = {"3.111+3", "6-8", "6*8", "6/8", "8/2", "20-5*3+50", "4e3+3", "2000+1.111111", "-1.0+9"};
        double[] results = {6.11, 2.0, 48.0, 0.75, 4.0, 55.0, 0, 20000001.11, 0.0};

        boolean[] result = new boolean[values.length];
        String[] wrongResults = new String[values.length];
        int correct = 0;

        calculator miniCalculator = new calculator();
        for (int i = 0; i < values.length; i++) {
            System.out.println(i);
            miniCalculator.setTextField(values[i]);
            miniCalculator.calc();
            result[i] = miniCalculator.getResult() == results[i] & results[i] == miniCalculator.getResult();
            if (result[i]) {
                correct++;
                wrongResults[i] = "";
            } else {
                wrongResults[i] = "" + miniCalculator.getResult();
            }
            System.err.flush();
        }
        System.out.println("MiniCalculatorTest >> Finished:\t\t\t" + correct + " out of " + result.length + " correctly.");
        if (correct != result.length) {
            System.out.println("MiniCalculatorTest >> results:\t\t\t" + Arrays.toString(result));
            System.out.println("MiniCalculatorTest >> wrong results:\t" + Arrays.toString(wrongResults));
        }

    }


}
