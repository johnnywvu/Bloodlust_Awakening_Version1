package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, dodgePressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W){ // up
            upPressed = true;
        }
        if (code == KeyEvent.VK_S){ // down
            downPressed = true;
        }
        if (code == KeyEvent.VK_A){ // left
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D){ // right
            rightPressed = true;
        }
        if (code == KeyEvent.VK_SPACE){
            dodgePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W){ // up
            upPressed = false;
        }
        if (code == KeyEvent.VK_S){ // down
            downPressed = false;
        }
        if (code == KeyEvent.VK_A){ // left
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D){ // right
            rightPressed = false;
        }
        if (code == KeyEvent.VK_SPACE){
            dodgePressed = false;
        }
    }
}
