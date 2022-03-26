package window;

import Phrases.Phrases;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser extends JFileChooser {

    public FileChooser(String currentDirectory) {
        super(currentDirectory);
        init();
    }

    public FileChooser() {
        super();
        init();
    }

    private void init() {
        this.removeChoosableFileFilter(this.getFileFilter());
        this.setFileFilter(new FileNameExtensionFilter("Money (*.money)", Phrases.EXTENSION));
        this.setMultiSelectionEnabled(false);}
}
