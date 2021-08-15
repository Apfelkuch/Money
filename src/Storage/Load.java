package Storage;

import Money.Entry;
import Money.Money;
import Phrases.Phrases;
import utilitis.Options;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;

public class Load {

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
                Entry entry = new Entry(
                        option,
                        Integer.parseInt(ev[1]),
                        LocalDate.parse(ev[2]),
                        ev[3],
                        ev[4],
                        ev[5],
                        Double.parseDouble(ev[6]),
                        Double.parseDouble(ev[7]),
                        Double.parseDouble(ev[8]),
                        money
                );
                money.getEntries().add(entry);
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
