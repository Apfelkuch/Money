package Storage;

import Money.Entry;
import Money.Money;
import Phrases.Phrases;
import utilitis.Options;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;

public class Load {

    // TODO need better loading because the data is to much for a String to handle
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

            // read control value
            String controlValue = content.substring(0, Phrases.CONTROL_VALUE.length());
            if (!controlValue.equals(Phrases.CONTROL_VALUE)) {
                throw new IllegalArgumentException("Save path does not contain the control value");
//                return false;
            }


            // read entries
            String[] entries = content.substring(Phrases.CONTROL_VALUE.length()).split(Character.toString(160) + Character.toString(160));

            for (String stringEntry : entries) {
                String[] ev = stringEntry.split(Character.toString(160));
                Options option;
                if (ev[0].equals(Options.INCOME.toString())) {
                    option = Options.INCOME;
                } else {
                    option = Options.SPENDING;
                }
                System.out.println(ev[3].hashCode());
                System.out.println(ev[4].hashCode());
                System.out.println(ev[5].hashCode());
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
}
