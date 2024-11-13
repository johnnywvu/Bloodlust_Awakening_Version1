package Main;

import objects.OBJ_dungeonKey;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font TNR_40, NP_80;
//    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean tutorialFinished = false;

    public UI(GamePanel gp){
        this.gp = gp;

        TNR_40 = new Font("Times New Roman", Font.PLAIN, 40);
        NP_80 = new Font("NIGHTMARE PILLS", Font.PLAIN, 80);
//        OBJ_dungeonKey key = new OBJ_dungeonKey(gp);
//        keyImage = key.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont(TNR_40);
        g2.setColor(Color.white);

        if (gp.gameState == gp.playState) {
            //
        }
        if (gp.gameState == gp.pauseState){
            drawPauseScreen();
        }
    }

    public void drawPauseScreen(){

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/ 2 - length/2;
        return x;
    }
}
