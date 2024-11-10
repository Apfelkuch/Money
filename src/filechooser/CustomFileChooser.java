package filechooser;

import phrases.Phrases;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class CustomFileChooser extends JFileChooser {

    public CustomFileChooser(String currentDirectory) {
        super(currentDirectory);
        init();
    }

    public CustomFileChooser() {
        super();
        init();
    }

    private void init() {
        this.resetChoosableFileFilters();
        this.removeChoosableFileFilter(this.getFileFilter());
        this.addChoosableFileFilter(new FileNameExtensionFilter(Phrases.EXTENSION_MONEY_TEXT, Phrases.EXTENSION_MONEY));
        // testing: set a file filter to show all files and not only ones with the money extension (needs to be removed for the release)
//        this.setAcceptAllFileFilterUsed(true);
        this.setMultiSelectionEnabled(false);

        // start with detail view instead of the list view
        Action details = this.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);
    }

    public File getSelectedFileMoneyFormat() {
        String path = getSelectedFile().getPath();
        String newPath = path.endsWith("." + Phrases.EXTENSION_MONEY) ? path : path.concat("." + Phrases.EXTENSION_MONEY);
        return new File(newPath);
    }

    public static void main(String[] args) {
        CustomFileChooser customFileChooser = new CustomFileChooser(System.getProperties().getProperty("user.home"));
        System.out.println("showDialog: " + customFileChooser.showDialog(null, "Jap"));
        System.out.println("showOpenDialog: " + customFileChooser.showOpenDialog(null));
        System.out.println("showSaveDialog: " + customFileChooser.showSaveDialog(null));
    }
}
