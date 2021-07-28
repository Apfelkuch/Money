package Save;

import Money.Entry;
import utilitis.Options;

import java.time.LocalDate;

public class Load {

    public static void load() {
        String s = "";

        // read control value


        // read entries
        String[] entries = s.split(Character.toString(160) + Character.toString(160));

        for (int i = 0; i < entries.length; i++) {
            String[] ev = entries[i].split(Character.toString(160));
            Options option;
            if (ev[0].equals(Options.INCOME.toString())) {
                option = Options.INCOME;
            } else {
                option = Options.SPENDING;
            }
            Entry entry = new Entry(option, Integer.parseInt(ev[1]), LocalDate.parse(ev[2]), ev[3], ev[4], ev[5], Double.parseDouble(ev[6]), Double.parseDouble(ev[7]), Double.parseDouble(ev[8]));
        }
    }
}
