package utilitis;

import Phrases.Phrases;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;

public class CustomArrowButton extends BasicArrowButton {

    public CustomArrowButton(int direction) {
        super(direction, Phrases.COLOR_BUTTON, null, Phrases.COLOR_BUTTON.darker(), null);
        super.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}
