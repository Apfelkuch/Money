package Save;

import Money.Money;
import Money.Entry;
import Phrases.Phrases;

import java.io.File;
import java.io.PrintWriter;

public class Save {

    public static boolean save(Money money, String path) {
        try {
            File saveFile = new File(path);
            if (!saveFile.getParentFile().isDirectory()) {
                if (!saveFile.getParentFile().mkdirs()) {
                    throw new IllegalArgumentException("Path directory is not be created");
                }
            }
            saveFile.createNewFile();
            if (print(saveFile, money)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean print(File file, Money money) {
        try {
            PrintWriter pw = new PrintWriter(file);

            // print control value
            pw.print(Phrases.CONTROL_VALUE);

            // save all the entries
            for (Entry entry : money.getEntries()) {
                pw.print(entry.toString());
                pw.print(Character.toString(160));
                pw.print(Character.toString(160));
            }

            pw.flush();
            pw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
