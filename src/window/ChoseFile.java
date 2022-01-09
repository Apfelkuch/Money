package window;

import Phrases.Phrases;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class ChoseFile {

    private String returnString = null;

    private final JDialog dialog;

    private final JPanel panel;

    public ChoseFile(String title, String[] paths, JFrame parent) {

        Font font = new Font("arial", Font.BOLD, 15);

        dialog = new JDialog(parent);
        dialog.setTitle(title);
        dialog.setModal(true);

        panel = new JPanel();

        panel.setLayout(new GridLayout(paths.length + 2, 1));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setForeground(Color.LIGHT_GRAY);

        // message

        JLabel labelNew = new JLabel(Phrases.startNew);
        labelNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    returnString = selectedFile.getPath();
                }
            }
        });
        labelNew.setFont(font);
        labelNew.setForeground(Color.BLACK);
        labelNew.setBackground(Color.GRAY);
        labelNew.setOpaque(true);
        labelNew.setHorizontalAlignment(JLabel.CENTER);
        panel.add(labelNew);

        for (String path : paths) {
            JLabel label = new JLabel(path + "\\" + Phrases.FILENAME);
            label.setName(path);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    returnString = label.getName();
                    dialog.dispose();
                }
            });
            label.setFont(font);
            label.setForeground(Color.BLACK);
            label.setBackground(Color.GRAY);
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);
            panel.add(label);
        }

        JLabel labelChose = new JLabel(Phrases.searchFile);
        labelChose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(".money", "money");
                fileChooser.setFileFilter(fileNameExtensionFilter); // setFileFilter

                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    returnString = fileChooser.getSelectedFile().getParentFile().getAbsolutePath();
                }
            }
        });
        labelChose.setFont(new Font("arial", Font.BOLD, 15));
        labelChose.setForeground(Color.BLACK);
        labelChose.setBackground(Color.GRAY);
        labelChose.setOpaque(true);
        labelChose.setHorizontalAlignment(JLabel.CENTER);
        panel.add(labelChose);

        dialog.setContentPane(panel);

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                returnString = null;
                dialog.dispose();
            }
        });

        dialog.setResizable(false);
    }

    public String inputDialog(Component parent) {
        dialog.pack();
        dialog.setSize(new Dimension(dialog.getWidth() < 300 ? 300 : dialog.getWidth(), dialog.getHeight()));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return returnString;
    }

    public static String inputDialog(JFrame parent, String title, String[] paths) {
        ChoseFile f = new ChoseFile(title, paths, parent);
        return f.inputDialog(parent);
    }

    public static void main(String[] args) {
        String[] paths = {
                "D:\\Git\\intelliJ\\Money\\Money\\save.money",
                "eee2",
                "ee"
        };
        System.out.println(ChoseFile.inputDialog(null, "test", paths));
    }

}
