package window;

import javax.swing.*;
import java.awt.*;

public class startingWindow extends JFrame {

    private final JProgressBar jProgressBar;

    public startingWindow() {
        super("Starting the program");
        this.setSize(new Dimension(300, 200));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("res\\money.png").getImage());

        ImageIcon imageIcon = new ImageIcon("res\\icons\\dark\\loading\\2x.png");

        JPanel panel = new JPanel(new BorderLayout());
        panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        this.add(panel);

        JLabel label = new JLabel("Loading...");
        label.setFont(new Font("arial", Font.BOLD, 30));
        label.setForeground(Color.BLACK);
        label.setIcon(imageIcon);
        label.setBackground(Color.GRAY);
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);

        jProgressBar = new JProgressBar();
        jProgressBar.setMinimum(0);
        jProgressBar.setMaximum(100);
        jProgressBar.setValue(0);
        jProgressBar.setBackground(label.getBackground());
        jProgressBar.setForeground(label.getForeground());
        jProgressBar.setBorderPainted(false);
        panel.add(jProgressBar, BorderLayout.PAGE_END);


        this.setVisible(true);
    }

    public void setProgressBarValue(int value) {
        this.jProgressBar.setValue(value);
    }

    public void setMaxProgressBar(int max) {
        jProgressBar.setMaximum(max);
    }

    public void addToProgressBar(int amount) {
        if (amount > this.jProgressBar.getMaximum() - this.jProgressBar.getValue()) {
            amount = this.jProgressBar.getMaximum();
        }
        this.jProgressBar.setValue(this.jProgressBar.getValue() + amount);
    }

    public static void main(String[] args) {
        int max = 1000;
        try {
            startingWindow startingWindow = new startingWindow();
            startingWindow.setMaxProgressBar(max);
            for (int i = 0; i <= max; i += 1) {
                startingWindow.setProgressBarValue(i);
                Thread.sleep(1);
            }
            startingWindow.dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
