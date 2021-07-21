package Input;

import window.Window;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyAdapterInput extends KeyAdapter {

    private final Window window;

    public KeyAdapterInput(Window window) {
        this.window = window;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("KeyAdapterInput.keyTyped >> e: " + e);
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            System.out.println("ENTER");
            window.transferFocus();
        }
    }
}
