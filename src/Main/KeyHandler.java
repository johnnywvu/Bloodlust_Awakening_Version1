package Main;

import javax.security.auth.kerberos.KeyTab;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    //DEBUG
    boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

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
        if (code == KeyEvent.VK_ESCAPE){ // enter pause state
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
            }
            else if (gp.gameState == gp.pauseState){
                gp.gameState = gp.playState;
            }
        }

        // DEBUG
        if (code == KeyEvent.VK_T){ // check drawTime
            if (!checkDrawTime) {
                checkDrawTime = true;
            } else if (checkDrawTime) {
                checkDrawTime = false;
            }
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
    }

}
