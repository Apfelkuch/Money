package window;

import Phrases.Phrases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
                FileChooser fileChooser = new FileChooser(System.getProperty("user.home"));
                if (fileChooser.showSaveDialog(null) == FileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getPath();
                    returnString = path.substring(path.lastIndexOf('.') + 1).equals(Phrases.EXTENSION) ? path : path.concat("." + Phrases.EXTENSION);
                    dialog.dispose();
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
            JLabel label = new JLabel(path);
            label.setName(path);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (new File(path).exists()) { // file can only be opened if the file exists
                        returnString = label.getName();
                        dialog.dispose();
                    }
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
                FileChooser fileChooser = new FileChooser();
                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == FileChooser.APPROVE_OPTION) {
                    returnString = fileChooser.getSelectedFile().getAbsolutePath();
                    dialog.dispose();
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

        dialog.setResizable(false);
    }

    public String inputDialog(Component parent) {
        dialog.pack();
        dialog.setSize(new Dimension(dialog.getWidth() < 300 ? 300 : dialog.getWidth(), dialog.getHeight()));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
//        System.out.println("ChoseFile.inputDialog >> returnString: " + returnString);
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
