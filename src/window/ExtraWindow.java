package window;

import phrases.Design;
import phrases.Phrases;
import utilitis.CustomJButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
    public ExtraWindow(String title, String message, int sort, Font font, Color colorBackground, Color colorForeground, boolean closable) {

        dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setIconImage(new ImageIcon("res\\money.png").getImage());
        dialog.setUndecorated(true);

        JPanel jpMain = new JPanel();
        JPanel jpMessage = new JPanel();
        JPanel jpButtons = new JPanel();

        jpMain.setLayout(new BorderLayout());
        jpMain.setBackground(colorBackground);
        jpMain.setForeground(colorForeground);
        jpMain.setBorder(new LineBorder(Phrases.BORDER, 2));

        jpMessage.setOpaque(false);
        jpButtons.setOpaque(false);

        jpMain.add(jpMessage, BorderLayout.CENTER);
        jpMain.add(jpButtons, BorderLayout.SOUTH);

        JMenuBar optionBar = new JMenuBar();
        optionBar.setLayout(new BorderLayout());
        jpMain.add(optionBar, BorderLayout.NORTH);

        int iconSize = 20;
        Image image = new ImageIcon("res\\money.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT);
        ImageIcon appIcon = new ImageIcon(image);
        JLabel app = new JLabel(appIcon);
        app.setPreferredSize(new Dimension(iconSize, iconSize));
        optionBar.add(app, BorderLayout.WEST);
        if (closable) {
            ImageIcon closeIcon = new ImageIcon("res\\icons\\icons8-löschen-24.png");
            CustomJButton close = new CustomJButton();
            close.setIcon(closeIcon);
            close.setPreferredSize(new Dimension(iconSize, iconSize));
            close.setBorderPainted(false);
            close.addActionListener(e -> {
                returnValue = EXIT_WITH_CANCEL;
                dialog.dispose();
            });
            optionBar.add(close, BorderLayout.EAST);
        }

        // message
        JLabel text = new JLabel(message);
        text.setFont(font);
        jpMessage.add(text);

        // buttons
        int buttonHeight = 30;
        CustomJButton positiveButton;
        CustomJButton negativeButton;
        if (sort == 0 || sort == 2) {
            positiveButton = new CustomJButton("ok");
            negativeButton = new CustomJButton("cancel");
        } else if (sort == 1) {
            positiveButton = new CustomJButton("yes");
            negativeButton = new CustomJButton("no");
        } else {
            positiveButton = new CustomJButton();
            negativeButton = new CustomJButton();
        }
        positiveButton.setFont(font);
        positiveButton.setPreferredSize(new Dimension(100, buttonHeight));
        positiveButton.setFocusable(false);

        negativeButton.setFont(font);
        negativeButton.setPreferredSize(new Dimension(100, buttonHeight));
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
    public static int messageDialog(Component parent, String title, String message, Font font, Color colorBackground, Color colorForeground, boolean closable) {
        ExtraWindow f = new ExtraWindow(title, message, 0, font, colorBackground, colorForeground, closable);
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
    public static int confirmDialog(Component parent, String title, String message, Font font, Color colorBackground, Color colorForeground, boolean closable) {
        ExtraWindow f = new ExtraWindow(title, message, 1, font, colorBackground, colorForeground, closable);
        return f.showDialog(parent);
    }

    /**
     * Show an input Dialog.
     *
     * @param parent          The parent of the dialog.
     * @param title           The title of the dialog window.
     * @param message         The message in the dialog.dialog.
     * @param font            The font which is used on the dialog.
     * @param colorBackground The background color.
     * @param colorForeground The foreground color.
     * @return NULL for no input or only a blank input, otherwise the returnString.
     */
    public static String inputDialog(Component parent, String title, String message, Font font, Color colorBackground, Color colorForeground, boolean closable) {
        ExtraWindow f = new ExtraWindow(title, message, 2, font, colorBackground, colorForeground, closable);
        return f.inputDialog(parent);
    }

    /**
     * Show an input Dialog.
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
    public static String inputDialogWithText(Component parent, String title, String message, String text, Font font, Color colorBackground, Color colorForeground, boolean closable) {
        ExtraWindow f = new ExtraWindow(title, message, 2, font, colorBackground, colorForeground, closable);
        f.input.setText(text);
        return f.inputDialog(parent);
    }

    public static void main(String[] args) {
        Phrases.init();
        Design.init();
        System.out.println(ExtraWindow.messageDialog(null, "Title", "message", Phrases.showFontBold, Phrases.BACKGROUND, Phrases.FOREGROUND, true));
        System.out.println(ExtraWindow.confirmDialog(null, "Title", "message", Phrases.showFontBold, Phrases.BACKGROUND, Phrases.FOREGROUND, true));
        System.out.println(ExtraWindow.inputDialog(null, "Title", "message", Phrases.showFontBold, Phrases.BACKGROUND, Phrases.FOREGROUND, true));
        System.out.println(ExtraWindow.inputDialogWithText(null, "Title", "message", "text", Phrases.showFontBold, Phrases.BACKGROUND, Phrases.FOREGROUND, true));
        System.out.println(ExtraWindow.messageDialog(null, "Title", "message", Phrases.showFontBold, Phrases.BACKGROUND, Phrases.FOREGROUND, false));
        System.out.println(ExtraWindow.confirmDialog(null, "Title", "message", Phrases.showFontBold, Phrases.BACKGROUND, Phrases.FOREGROUND, false));
        System.out.println(ExtraWindow.inputDialog(null, "Title", "message", Phrases.showFontBold, Phrases.BACKGROUND, Phrases.FOREGROUND, false));
        System.out.println(ExtraWindow.inputDialogWithText(null, "Title", "message", "text", Phrases.showFontBold, Phrases.BACKGROUND, Phrases.FOREGROUND, false));
    }

}