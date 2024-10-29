package window;

import FileChooser.CustomFileChooser;
import Phrases.Phrases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class FileChooser {
    private String selectedFileOption = null;

    private final JDialog dialog;

    public FileChooser(String title, String[] paths, JFrame parent) {

        Font font = new Font("arial", Font.BOLD, 15);

        dialog = new JDialog(parent);
        dialog.setTitle(title);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(paths.length + 2, 1));

        JLabel newFile = new JLabel(Phrases.startNew);
        newFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CustomFileChooser customFileChooser = new CustomFileChooser(System.getProperty("user.home"));
                if (customFileChooser.showSaveDialog(dialog) == CustomFileChooser.APPROVE_OPTION) {
                    selectedFileOption = customFileChooser.getSelectedFileMoneyFormat().getPath();
                    dialog.dispose();
                }
            }
        });
        designLabel(newFile, font);
        panel.add(newFile);

        for (String path : paths) {
            JLabel fileFromPath = new JLabel(path);
            fileFromPath.setName(path);
            fileFromPath.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (new File(path).exists()) { // file can only be opened if the file exists
                        selectedFileOption = fileFromPath.getName();
                        dialog.dispose();
                    } else {
                        System.err.println("[FileChooser] selected file does not exist! (Path: " + path + ")");
                    }
                }
            });
            designLabel(fileFromPath, font);
            panel.add(fileFromPath);
        }

        JLabel searchForFile = new JLabel(Phrases.searchFile);
        searchForFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CustomFileChooser customFileChooser = new CustomFileChooser();
                if (customFileChooser.showOpenDialog(dialog) == CustomFileChooser.APPROVE_OPTION) {
                    selectedFileOption = customFileChooser.getSelectedFileMoneyFormat().getPath();
                    dialog.dispose();
                }
            }
        });
        designLabel(searchForFile, font);
        panel.add(searchForFile);

        dialog.setContentPane(panel);

        dialog.pack();
        dialog.setSize(new Dimension(Math.max(300, dialog.getWidth()), dialog.getHeight()));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private void designLabel(JLabel label, Font font) {
        label.setFont(font);
        label.setForeground(Phrases.FOREGROUND);
        label.setBackground(Phrases.BACKGROUND);
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
    }

    public String getSelectedFileOption() {
        return selectedFileOption;
    }

    public static void main(String[] args) {
        Phrases.init();
        String[] paths = {
                "D:\\Git\\intelliJ\\Money\\Money\\save.money",
                "eee2",
                "C:\\Users\\Apfel\\Documents\\Studium\\Ingenieurinformatik\\Notenschnitt.xlsx",
                "ee"
        };
        FileChooser fileChooser = new FileChooser("test", paths, null);
        System.out.println(fileChooser.getSelectedFileOption());
        System.exit(0);
    }

}
