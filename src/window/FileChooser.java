package window;

import Phrases.Phrases;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

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
        this.setMultiSelectionEnabled(false);
    }

    @Override
    protected JDialog createDialog(Component parent) throws HeadlessException {
        JDialog jDialog = super.createDialog(parent);
        jDialog.setIconImage(new ImageIcon("res\\money.png").getImage());
        return jDialog;
    }
}
