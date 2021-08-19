package Storage;

import Money.Entry;
import Money.Money;
import Phrases.Phrases;
import utilitis.Options;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;

public class Load {

    // improve for better loading because the amount of data can grow infinitely
    public static boolean load(Money money, String path, int maxContentElements) {
        try {
            FileReader fileReader = new FileReader(path);
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
//                return false;
            }


            // read entries
            String[] entries = content.substring(Phrases.CONTROL_VALUE.length()).split(Character.toString(160) + Character.toString(160));

            for (String stringEntry : entries) {
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
//        System.out.println("Load.LoadEntry >> ev[3].hashCode(): " + ev[3].hashCode());
//        System.out.println("Load.LoadEntry >> ev[4].hashCode(): " + ev[4].hashCode());
//        System.out.println("Load.LoadEntry >> ev[5].hashCode(): " + ev[5].hashCode());
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

//    public static void main(String[] args) {
//        Load.load("D:\\Git\\intelliJ\\Money\\Money\\save.txt", 1000, null, 6);
//    }
//
//    public static boolean load(String path, int buffer, Money money, int maxContentElements) {
//        try {
//            FileReader fileReader = new FileReader(path);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            ArrayList<Character> chars = new ArrayList<>();
//            boolean control = false;
//            while (true) {
//                int a = bufferedReader.read();
//                if (a == -1) { // jump in if the file is completely read and processed
//                    // load the last entries on the table
////                    int size = money.getEntries().size();
////                    int pos = size - maxContentElements;
////                    if (pos < 0) {
////                        pos = 0;
////                    }
////                    money.moveTopEntry(pos);
//                    fileReader.close();
//                    bufferedReader.close();
//                    return true;
//                }
//                chars.add(Character.toChars(a)[0]);
//                if (chars.size() >= buffer) {
//                    // work with the text
//                    StringBuilder s = new StringBuilder();
//                    for (char c : chars) {
//                        s.append(c);
//                    }
//                    if (!control) {
//                        // read control value
//                        System.out.println(s);
//                        String controlValue = s.substring(0, Phrases.CONTROL_VALUE.length());
//                        for (Character c : controlValue.toCharArray()) {
//                            System.out.println(c.hashCode());
//                        }
//                        if (!controlValue.equals(Phrases.CONTROL_VALUE)) {
//                            throw new IllegalArgumentException("Save path does not contain the control value");
//                        }
//                        control = true;
////                        for (int i = 0; i < controlValue.length(); i++) {
////                            chars.remove(0);
////                        }
//                        chars.subList(0, Phrases.CONTROL_VALUE.length()).clear();
//                        continue;
//                    }
//                    System.out.print(s.toString());
//                    chars.clear();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}
