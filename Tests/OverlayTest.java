import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class OverlayTest extends Window{

    public static OverlayTest overlayTest;

    private JTextField textField;

    public static void main(String[] args) {
        JFrame jframe = new JFrame("test");
        jframe.setSize(300, 200);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jframe.add(jPanel);

        JButton test = new JButton("test");
        test.setSize(100, 25);
        test.addActionListener(e -> {
            if (overlayTest == null) {
                overlayTest = new OverlayTest(test.getLocationOnScreen(), jframe);
            }
        });
        test.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (overlayTest != null) {
                    overlayTest.keyTyped(e);
                }
            }
        });
        jPanel.add(test, BorderLayout.CENTER);

        for (int i = 0; i < 10; i++) {
            JButton button = new JButton("" + i);
            button.setSize(100, 25);
            jPanel.add(button, BorderLayout.CENTER);
        }

        jframe.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (overlayTest != null) {
                    overlayTest.setLocation(test.getLocationOnScreen());
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (overlayTest != null) {
                    overlayTest.setLocation(test.getLocationOnScreen());
                }
            }
        });

        jframe.setVisible(true);

    }

    public OverlayTest(Point location, JFrame frame) {
        // window setup
        super(frame);

        this.setSize(100, 125);
        this.setLocation(location.x + 50, location.y + 50);
        this.setBackground(Color.BLUE);
        this.setVisible(true);

        build();
    }

    public void build() {
        // build GUI
        JPanel jPanel = new JPanel();
        jPanel.setSize(100, 100);
        jPanel.setLayout(null);
        this.add(jPanel);

        JButton close = new JButton("close");
        close.setSize(new Dimension(100, 25));
        close.addActionListener(e -> {
            this.dispose();
            overlayTest = null;
        });
        close.setLocation(0, 0);
        close.requestFocus();
        close.requestFocusInWindow();
        jPanel.add(close);

        JButton button = new JButton("Test");
        button.setSize(close.getSize());
        button.setLocation(0, 25);
        jPanel.add(button);

        textField = new JTextField();
        textField.setSize(new Dimension(100, 25));
        textField.setLocation(0, 50);
        jPanel.add(textField);
    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_DELETE || e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
        } else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            this.dispose();
            overlayTest = null;
        } else if (Character.isDefined(e.getKeyChar())) {
            textField.setText(textField.getText() + e.getKeyChar());
        }
    }
}
