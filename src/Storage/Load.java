package Storage;

import Money.Entry;
import Money.Money;
import Phrases.Phrases;
import utilitis.Options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;

public class Load {

    // improve for better loading because the amount of data can grow infinitely
    public static boolean load(Money money, String path, int maxContentElements) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("No file found");
                return false;
            }

            FileReader fileReader = new FileReader(path, Phrases.CHARSET);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String s;
            StringBuilder content = new StringBuilder();
            while (true) {
                s = bufferedReader.readLine();
                if (s == null) break;
                content.append(s);
            }
//            System.out.println("Load.load >> content.length(): " + content.length());

            // read control value
            String controlValue = content.substring(0, Phrases.CONTROL_VALUE.length());
            if (!controlValue.equals(Phrases.CONTROL_VALUE)) {
                throw new IllegalArgumentException("Save path does not contain the control value");
            }


            // read entries
            String[] entries = content.substring(Phrases.CONTROL_VALUE.length()).split(Character.toString(160) + Character.toString(160));

            for (String stringEntry : entries) {
                if (!stringEntry.isBlank())
                    Load.LoadEntry(stringEntry, money);
            }

            // load the last entries on the table
            int size = money.getEntries().size();
            int pos = size - maxContentElements;
            if (pos < 0) {
                pos = 0;
            }
            money.moveTopEntry(pos);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Converts a String to an Entry and integrate the entry into money.
     *
     * @param stringEntry The Entry data in form of a String.
     * @param money       The place where the entry is integrated.
     */
    private static void LoadEntry(String stringEntry, Money money) {
        String[] ev = stringEntry.split(Character.toString(160));
        Options option;
        if (ev[0].equals(Options.INCOME.toString())) {
            option = Options.INCOME;
        } else {
            option = Options.SPENDING;
        }
        String receiveBy = ev[3].equals(Character.toString(177)) ? "" : ev[3];
        String category = ev[4].equals(Character.toString(177)) ? "" : ev[4];
        String purpose = ev[5].equals(Character.toString(177)) ? "" : ev[5];
        Entry entry = new Entry(
                option,                     // option
                Integer.parseInt(ev[1]),    // number
                LocalDate.parse(ev[2]),     // date
                receiveBy,                  // receiveBy
                category,                   // category
                purpose,                    // purpose
                Double.parseDouble(ev[6]),  // spending
                Double.parseDouble(ev[7]),  // income
                Double.parseDouble(ev[8]),  // balance
                money                       // reference to money
        );
        money.getEntries().add(entry);
        // load the JComboBoxDate
        money.addToPreListReceiverBy(receiveBy);
        money.addToPreListCategories(category);
        money.addToPreListPurpose(purpose);
    }

    /**
     * Loads the paths of possible save locations into the program.
     *
     * @param path The path where the paths are saved.
     * @return A Array List with Strings which contains all paths.
     */
    public static ArrayList<String> loadPaths(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("No file found");
                return new ArrayList<>();
            }

            FileReader fileReader = new FileReader(path, Phrases.CHARSET);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // read control value
            String controlValue = bufferedReader.readLine();
            if (!controlValue.equals(Phrases.CONTROL_VALUE)) {
                throw new IllegalArgumentException("Save path does not contain the control value");
            }

            ArrayList<String> paths = new ArrayList<>();
            while (true) {
                String s = bufferedReader.readLine();
                if (s == null) break;
                paths.add(s);
            }
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
