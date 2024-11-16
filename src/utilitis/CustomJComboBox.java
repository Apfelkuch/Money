package utilitis;

import window.Window;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.BadLocationException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @param <Type> The Type of the JComboBox
 * @see <a href="https://www.tutorialspoint.com/how-can-we-implement-auto-complete-jcombobox-in-java">https://www.tutorialspoint.com/how-can-we-implement-auto-complete-jcombobox-in-java</a>
 */
public class CustomJComboBox<Type> extends JComboBox<Type> {
    private int caretPos = 0;
    private JTextField textField = null;

    private Window window;

    public CustomJComboBox(Window window) {
        super();
        setEditor(new BasicComboBoxEditor());
        setEditable(true);
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                return new CustomArrowButton(BasicArrowButton.SOUTH);
            }
        });
        this.window = window;
    }

    @Override
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        textField.setText(getItemAt(index) == null ? "" : getItemAt(index).toString());
        textField.setSelectionEnd(caretPos + textField.getText().length());
        try {
            textField.moveCaretPosition(caretPos);
        } catch (IllegalArgumentException e) {
            textField.moveCaretPosition(0);
        }
    }

    @Override
    public void setEditor(ComboBoxEditor editor) {
        super.setEditor(editor);
        if (editor.getEditorComponent() instanceof JTextField) {
            textField = (JTextField) editor.getEditorComponent();
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent event) {
                    char key = event.getKeyChar();
                    if (key == '\n') {
                        if (window != null) {
                            window.addEntry();
                        }
                        return;
                    }
                    if (!(Character.isLetterOrDigit(key) || Character.isSpaceChar(key))) return;
                    caretPos = textField.getCaretPosition();
                    String text = "";
                    try {
                        text = textField.getText(0, caretPos);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < getItemCount(); i++) {
                        String element = (String) getItemAt(i);
                        if (element.startsWith(text)) {
                            setSelectedIndex(i);
                            showPopup();
                            return;
                        }
                    }
                }
            });

        }
    }

    public void setWindow(Window window) {
        this.window = window;
    }

}


