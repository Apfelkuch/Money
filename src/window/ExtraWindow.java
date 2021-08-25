package window;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;

public class ExtraWindow {

    public static final int EXIT = 0;
    public static final int EXIT_WITH_OK = 1;
    public static final int EXIT_WITH_CANCEL = 2;
    public static final int EXIT_WITH_YES = 3;
    public static final int EXIT_WITH_NO = 4;

    private int returnValue = EXIT;
    private String returnString = null;

    private final JDialog dialog;

    private JTextField input = null;

    private final JLabel text;

    private JButton positiveButton = null;
    private JButton negativeButton = null;

    private final JPanel jpMain;
    private final JPanel jpMessage;
    private final JPanel jpButtons;

    /**
     * Construct the Dialog window with all parts.
     *
     * @param title           Title of the dialog window.
     * @param message         The Message in the dialog.
     * @param sort            The sort of dialog ==> 0 = message, 1 = confirm, 2 = input
     * @param font            The font which is used on the dialog.
     * @param colorBackground The background color.
     * @param colorForeground The foreground color.
     */
    public ExtraWindow(String title, String message, int sort, Font font, Color colorBackground, Color colorForeground) {

        dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);

        jpMain = new JPanel();
        jpMessage = new JPanel();
        jpButtons = new JPanel();

        jpMain.setLayout(new BorderLayout());
        jpMain.setBackground(colorBackground);
        jpMain.setForeground(colorForeground);

        jpMessage.setOpaque(false);
        jpButtons.setOpaque(false);

        jpMain.add(jpMessage, BorderLayout.CENTER);
        jpMain.add(jpButtons, BorderLayout.SOUTH);

        // message
        text = new JLabel(message);
        text.setFont(font);
        jpMessage.add(text);

        // buttons
        if (sort == 0 || sort == 2) {
            positiveButton = new JButton("ok");
            negativeButton = new JButton("cancel");
        } else if (sort == 1) {
            positiveButton = new JButton("yes");
            negativeButton = new JButton("no");
        }
        positiveButton.setFont(font);
        positiveButton.setPreferredSize(new Dimension(100, 30));
        positiveButton.setFocusable(false);

        negativeButton.setFont(font);
        negativeButton.setPreferredSize(new Dimension(100, 30));
        negativeButton.setFocusable(false);

        jpButtons.add(positiveButton);
        jpButtons.add(negativeButton);

        // key listener
        KeyListener keylistener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (sort == 0 || sort == 2) {
                        returnValue = EXIT_WITH_OK;
                    } else if (sort == 1) {
                        returnValue = EXIT_WITH_YES;
                    }
                    dialog.dispose();
                }
            }
        };

        // input
        if (sort == 2) {
            input = new JTextField();
            input.setPreferredSize(new Dimension(200, 20));
            input.setFont(font);
            input.setBorder(new LineBorder(Color.BLACK, 2, false));
            input.setEditable(true);
            input.setOpaque(false);
            input.addKeyListener(keylistener);
            jpMessage.add(input);
        }

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                returnValue = EXIT;
            }
        });

        positiveButton.addActionListener(e -> {
            if (sort == 0 || sort == 2) {
                returnValue = EXIT_WITH_OK;
            } else if (sort == 1) {
                returnValue = EXIT_WITH_YES;
            }
            if (sort == 2) {
                returnString = (input.getText().isBlank()) ? null : input.getText();
            }
            dialog.dispose();
        });

        negativeButton.addActionListener(e -> {
            if (sort == 0 || sort == 2) {
                returnValue = EXIT_WITH_CANCEL;
            } else if (sort == 1) {
                returnValue = EXIT_WITH_NO;
            }
            dialog.dispose();
        });

        dialog.setContentPane(jpMain);

        dialog.addKeyListener(keylistener);

        dialog.setResizable(false);
    }

    /**
     * Show the Dialog. and return the returnValue.
     *
     * @param parent Parent of the dialog.
     * @return EXIT_WITH_OK (1) for ok, EXIT_WITH_CANCEL (2) for cancel, EXIT_WITH_YES (3) for yes, EXIT_WITH_NO (4) for no.
     */
    public int showDialog(Component parent) {
        dialog.pack();
        dialog.setSize(new Dimension(Math.max(dialog.getWidth(), 300), dialog.getHeight()));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return returnValue;
    }

    /**
     * Show the Dialog. and return the returnString.
     *
     * @param parent Parent of the dialog.
     * @return NULL for no input or only a blank input, otherwise the returnString.
     */
    public String inputDialog(Component parent) {
        dialog.pack();
        dialog.setSize(new Dimension(dialog.getWidth() < 300 ? 300 : dialog.getWidth(), dialog.getHeight()));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return returnString;
    }

    /**
     * Show a message Dialog.
     *
     * @param parent          The parent of the dialog.
     * @param title           The title of the dialog window.
     * @param message         The message in the dialog.
     * @param font            The font which is used on the dialog.
     * @param colorBackground The background color.
     * @param colorForeground The foreground color.
     * @return EXIT_WITH_OK (1) for ok, EXIT_WITH_CANCEL (2) for cancel.
     */
    public static int messageDialog(Component parent, String title, String message, Font font, Color colorBackground, Color colorForeground) {
        ExtraWindow f = new ExtraWindow(title, message, 0, font, colorBackground, colorForeground);
        return f.showDialog(parent);
    }

    /**
     * Show a confirm Dialog.
     *
     * @param parent          The parent of the dialog.
     * @param title           The title of the dialog window.
     * @param message         The message in the dialog.dialog.
     * @param font            The font which is used on the dialog.
     * @param colorBackground The background color.
     * @param colorForeground The foreground color.
     * @return EXIT_WITH_YES (3) for yes, EXIT_WITH_NO (4) for no.
     */
    public static int confirmDialog(Component parent, String title, String message, Font font, Color colorBackground, Color colorForeground) {
        ExtraWindow f = new ExtraWindow(title, message, 1, font, colorBackground, colorForeground);
        return f.showDialog(parent);
    }

    /**
     * Show a input Dialog.
     *
     * @param parent          The parent of the dialog.
     * @param title           The title of the dialog window.
     * @param message         The message in the dialog.dialog.
     * @param font            The font which is used on the dialog.
     * @param colorBackground The background color.
     * @param colorForeground The foreground color.
     * @return NULL for no input or only a blank input, otherwise the returnString.
     */
    public static String inputDialog(Component parent, String title, String message, Font font, Color colorBackground, Color colorForeground) {
        ExtraWindow f = new ExtraWindow(title, message, 2, font, colorBackground, colorForeground);
        return f.inputDialog(parent);
    }

    /**
     * Show a input Dialog.
     *
     * @param parent          The parent of the dialog.
     * @param title           The title of the dialog window.
     * @param message         The message in the dialog.
     * @param text            The text, which is already in the input field.
     * @param font            The font which is used on the dialog.
     * @param colorBackground The background color.
     * @param colorForeground The foreground color.
     * @return NULL for no input or only a blank input, otherwise the returnString.
     */
    public static String inputDialogWithText(Component parent, String title, String message, String text, Font font, Color colorBackground, Color colorForeground) {
        ExtraWindow f = new ExtraWindow(title, message, 2, font, colorBackground, colorForeground);
        f.input.setText(text);
        return f.inputDialog(parent);
    }

}