package window;

import Phrases.Phrases;
import utilitis.CustomJLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class startingWindow extends JFrame {

    private final JProgressBar jProgressBar;
    private final CustomJLabel loadingText;

    public startingWindow() {
        super("Starting the program");
        this.setSize(new Dimension(300, 200));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/money.png"))).getImage());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadingText.cancelAnimation();
            }
        });
        this.setCursor(Cursor.WAIT_CURSOR);

        this.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout());
        this.add(panel);

        loadingText = new CustomJLabel();
        loadingText.setText("Loading...");
        loadingText.setFont(new Font("arial", Font.BOLD, 30));
        loadingText.setForeground(Color.BLACK);
        loadingText.setAnimatedIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/dark/loading/2x.png"))), Phrases.periodForAnimatedIcon);
        loadingText.setBackground(Color.GRAY);
        loadingText.setOpaque(true);
        panel.add(loadingText, BorderLayout.CENTER);

        jProgressBar = new JProgressBar();
        jProgressBar.setMinimum(0);
        jProgressBar.setMaximum(100);
        jProgressBar.setValue(0);
        jProgressBar.setPreferredSize(new Dimension(-1, 15));
        jProgressBar.setBackground(loadingText.getBackground());
        jProgressBar.setForeground(loadingText.getForeground());
        jProgressBar.setBorder(null);
        jProgressBar.setBorderPainted(false);
        panel.add(jProgressBar, BorderLayout.SOUTH);

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
