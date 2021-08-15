package window;

import javax.swing.*;
import java.awt.*;

public class startingWindow extends JFrame {

    public startingWindow() {
        super("Starting the program");
        this.setSize(new Dimension(300, 200));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        ImageIcon imageIcon = new ImageIcon("res\\icons\\dark\\loading\\2x.png");

        JLabel label = new JLabel("Loading...");
        label.setFont(new Font("arial", Font.BOLD, 30));
        label.setForeground(Color.BLACK);
        label.setIcon(imageIcon);
        label.setBackground(Color.GRAY);
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label);



        this.setVisible(true);
    }
}
