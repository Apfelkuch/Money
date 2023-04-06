package utilitis;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class InputField_fixes extends JTextField {

    private String prefix = "";
    private String postfix = "";
    private boolean focused = false;

    public InputField_fixes() {
        this("");
    }

    public InputField_fixes(String text) {
        this.setValue(text);
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (focused)
                    return;
                focused = true;
                InputField_fixes.super.setText(getValue());
                InputField_fixes.super.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!focused)
                    return;
                focused = false;
                setText(InputField_fixes.super.getText());
            }
        });
    }

    public String getValue() {
        return super.getText().substring(prefix.length(), super.getText().length() - postfix.length());
    }

    public void setValue(String content) {
        if (focused) {
            super.setText(content);
        } else {
            this.setText(content);
        }
    }

    public String getText() {
        return super.getText();
    }

    @Override
    public void setText(String text) {
        super.setText(prefix + text + postfix);
    }

    // GETTER && SETTER

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }
}
