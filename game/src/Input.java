import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Input implements KeyListener, FocusListener {
    final private int MAX_KEY_ID = 128;
    private boolean[] pressedKeys = new boolean[MAX_KEY_ID];

    public Input(JFrame frame) {
        frame.addKeyListener(this);
    }

    public boolean IsKeyPressed(int key) {
        if (MAX_KEY_ID <= key) {
            return false;
        }
        return pressedKeys[key];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void handleEvent(int key, boolean press) {
        if (MAX_KEY_ID <= key) {
            return;
        }
        pressedKeys[key] = press;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        handleEvent(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleEvent(e.getKeyCode(), false);
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        for (int i = 0; i < MAX_KEY_ID; ++i) {
            pressedKeys[i] = false;
        }
    }
}
