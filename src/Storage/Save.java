package Storage;

import Money.Entry;
import Money.Money;
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
            PrintWriter pw = new PrintWriter(file, Phrases.CHARSET);

            // print control value
            pw.print(Phrases.CONTROL_VALUE);

            // save all the entries
            for (Entry entry : money.getEntries()) {
                pw.print(entry.toString());
                pw.print(Phrases.DIVIDER);
                pw.print(Phrases.DIVIDER);
            }

            pw.flush();
            pw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveFiles(String path, String[] paths) {
        try {
            File file = new File(path);
            if (!file.getParentFile().isDirectory()) {
                if (!file.getParentFile().mkdirs()) {
                    throw new IllegalArgumentException("Path directory is not be created");
                }
            }
            file.createNewFile();

            // print to paths to the file
            PrintWriter printWriter = new PrintWriter(file, Phrases.CHARSET);
            printWriter.println(Phrases.CONTROL_VALUE);
            for (String s : paths) {
                printWriter.println(s);
            }

            printWriter.flush();
            printWriter.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
