package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed; // DIRECTIONS
    public boolean onePressed, twoPressed, threePressed, fourPressed; // GEAR
    public boolean enterPressed, spacePressed; // OTHERS

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

        // TITLE STATE
        if (gp.gameState == gp.titleState){
            if (code == KeyEvent.VK_W){ // up
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_S){ // down
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2){
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER){
                if (gp.ui.commandNum == 0){
                    gp.gameState = gp.playState;
                }
                if(gp.ui.commandNum == 1){
                    // add later
                }
                if (gp.ui.commandNum == 2){
                    System.exit(0);
                }
            }
        }

//         PLAY STATE
        if(gp.gameState == gp.playState){
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
                gp.gameState = gp.pauseState;
            }
            if (code == KeyEvent.VK_ENTER){ // interaction with objects/npcs
                enterPressed = true;
            }

            // DASH
            if (code == KeyEvent.VK_SPACE){
                spacePressed = true;
            }

            // GEAR SELECTION
            if (code == KeyEvent.VK_1){
                onePressed = true;
            }
            if (code == KeyEvent.VK_2){
                twoPressed = true;
            }
            if (code == KeyEvent.VK_3){
                threePressed = true;
            }
            if (code == KeyEvent.VK_4){
                fourPressed = true;
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

//        PAUSE STATE
        else if (gp.gameState == gp.pauseState){
            if (code == KeyEvent.VK_ESCAPE){ // enter pause state
                gp.gameState = gp.playState;
            }
        }

//        DIALOGUE STATE
        else if (gp.gameState == gp.dialogueState){
            if (code == KeyEvent.VK_ENTER){
                gp.gameState = gp.playState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE){
            spacePressed = false;
        }

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

        if (code == KeyEvent.VK_1){
            onePressed = false;
        }
        if (code == KeyEvent.VK_2){
            twoPressed = false;
        }
        if (code == KeyEvent.VK_3){
            threePressed = false;
        }
        if (code == KeyEvent.VK_4){
            fourPressed = false;
        }
    }

}
