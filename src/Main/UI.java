package Main;

import Entity.Entity;
import objects.OBJ_Heart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font TNR_40, NP_80;
    BufferedImage heart_full, heart_half, heart_empty;
    public boolean messageOn = false;
    public String message = "";
    public String currentDialogue = "";
    private BufferedImage titleScreenImage, cursor, dashbar;
    private BufferedImage defaultSelected, meleeSelected, rangeSelected, mageSelected, shieldSelected;
    double screenImageX;
    double screenImageY;
    public int commandNum = 0;
    int directionCounter = 0;
    double screenImageSpeed = 0.3;

    private float dashBarCurrent = 0;   // To receive the current dash value from the Player class



    public UI(GamePanel gp){
        this.gp = gp;

        TNR_40 = new Font("Times New Roman", Font.PLAIN, 40);
        NP_80 = new Font("NIGHTMARE PILLS", Font.PLAIN, 80);

        titleScreenImage = setup("/title/titleImageShrunk");
        cursor = setup("/title/cursor");
        dashbar = setup("/UI/dashbar");

        getGearSelectionImage();

        screenImageX = 0;
        screenImageY = 0;

        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_empty = heart.image3;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(TNR_40);
        g2.setColor(Color.white);

        // TITLE STATE
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        // PLAY STATE
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawGearSelector();
            drawDashBar();
            //
        }

        // PAUSE STATE
        if (gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
            drawDashBar();
        }

        // DIALOGUE STATE
        if (gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
        }
    }

    public void updateDashBar(float dashCurrent) {
        this.dashBarCurrent = dashCurrent;  // Update the dash bar value from Player class
    }

    public void drawDashBar() {

        if (gp.player.isDashing){

        }

        int dashBarX = gp.tileSize / 2;          // X position of the dash bar on screen
        int dashBarY = gp.tileSize * 2;          // Y position of the dash bar
        int dashBarWidth = 200;     // Width of the dash bar
        int dashBarHeight = 40;     // Height of the dash bar
//        g2.setColor(Color.RED);
//        g2.fillRect(dashBarX, dashBarY, dashBarWidth, dashBarHeight);  // Background of the bar

        if (dashBarCurrent == gp.player.dashBarMax){
            g2.setColor(new Color(255, 248, 89, 255));
        } else {
            g2.setColor(Color.white);
        }
        int width = (int) (dashBarWidth * (dashBarCurrent / gp.player.dashBarMax));  // Calculate the width based on current fill
        g2.fillRect(dashBarX + 35, dashBarY + 10, width, dashBarHeight);  // Filled portion

        g2.drawImage(dashbar,dashBarX,dashBarY , dashbar.getWidth() * 2, dashbar.getHeight() * 2,null);
    }


    public void drawGearSelector(){
        BufferedImage image = null;

        int x = gp.tileSize * 12 + (gp.tileSize /2);
        int y = gp.tileSize * 8 + (gp.tileSize / 2);

        switch (gp.player.gearSelected){
            case 0:
                image = defaultSelected;
                break;
            case 1:
                image = meleeSelected;
                break;
            case 2:
                image = rangeSelected;
                break;
            case 3:
                image = mageSelected;
                break;
            case 4:
                image = shieldSelected;
                break;
        }

        g2.drawImage(image,x,y,image.getWidth() * gp.scale, image.getHeight() * gp.scale, null);

    }

    public void drawPlayerLife(){

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        // DRAW BLANK HEART
        while (i < gp.player.maxLife / 2){
            g2.drawImage(heart_empty, x, y, null);
            i++;
            x += gp.tileSize + gp.tileSize/8;
        }

        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        while (i < gp.player.life){
            g2.drawImage(heart_half,x,y, null);
            i++;
            if (i < gp.player.life){
                g2.drawImage(heart_full,x,y,null);
            }
            i++;
            x += gp.tileSize + gp.tileSize/8;
        }

    }

    public BufferedImage setup(String path){
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream( path + ".png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void getGearSelectionImage(){
        meleeSelected = setup("/UI/melee-selected");
        rangeSelected = setup("/UI/range-selected");
        mageSelected = setup("/UI/magic-selected");
        shieldSelected = setup("/UI/shield-selected");
        defaultSelected = setup("/UI/default-selection");
    }

    public void drawTitleScreen(){
        int newWidth = getTitleScreenCalculations(titleScreenImage,"width"); // 1024 px wide && frame 768 px wide
        int newHeight = getTitleScreenCalculations(titleScreenImage,"height");

        g2.drawImage(titleScreenImage, (int) screenImageX, (int) screenImageY, newWidth, newHeight, null);

        if (directionCounter == 0){ // GO LEFT
            screenImageX -= screenImageSpeed;
            if (screenImageX + newWidth <= gp.screenWidth){
                directionCounter = 1;
            }
        } else if (directionCounter == 1) { // GO RIGHT
            screenImageX += screenImageSpeed;
            if ( (int) screenImageX == 0){
                directionCounter = 0;
            }
        }

        // TITLE NAME
        g2.setFont(NP_80);
        String text1 = "Bloodlust:";
        int x = getXforCenteredText(text1);
        int y = gp.tileSize * 2;
        // TEXT 1 SHADOW
        g2.setColor(Color.BLACK);
        g2.drawString(text1, x+3, y+3);
        // TEXT 1
        g2.setColor(Color.white);
        g2.drawString(text1, x, y);


        String text2 = "Awakening";
        x = getXforCenteredText(text2);
        y += gp.tileSize * 2;
        // TEXT 2 SHADOW
        g2.setColor(Color.BLACK);
        g2.drawString(text2, x+3, y+3);
        // TEXT 2
        g2.setColor(new Color(0x8b0000));
        g2.drawString(text2, x, y);

        // MENU
        g2.setFont(TNR_40);

        text1 = "New Game";
        x = getXforCenteredText(text1);
        y += gp.tileSize * 4;
        g2.setColor(Color.BLACK);
        g2.drawString(text1, x+3, y+3);
        g2.setColor(Color.WHITE);
        g2.drawString(text1, x,y);
        if (commandNum == 0){
            g2.drawImage(cursor, x - (gp.tileSize / 2), y - 20,null);
        }

        text1 = "Load Game";
        x = getXforCenteredText(text1);
        y += gp.tileSize * 1;
        g2.setColor(Color.BLACK);
        g2.drawString(text1, x+3, y+3);
        g2.setColor(Color.WHITE);
        g2.drawString(text1, x,y);
        if (commandNum == 1){
            g2.drawImage(cursor, x - (gp.tileSize / 2), y - 20,null);
        }

        text1 = "Exit";
        x = getXforCenteredText(text1);
        y += gp.tileSize * 1;
        g2.setColor(Color.BLACK);
        g2.drawString(text1, x+3, y+3);
        g2.setColor(Color.WHITE);
        g2.drawString(text1, x,y);
        if (commandNum == 2){
            g2.drawImage(cursor, x - (gp.tileSize / 2), y - 20,null);
        }
    }

    public void drawDialogueScreen(){

        // WINDOW
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        drawSubWindow(x,y,width,height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line: currentDialogue.split("\n")){
            g2.drawString(line,x,y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color color = new Color(0,0,0,210);
        g2.setColor(color);
        g2.fillRoundRect(x,y,width,height,35,35);

        color = new Color(255,255,255);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);

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
    
    public int getTitleScreenCalculations(BufferedImage image, String dimension){
        if (image == null){
            throw new IllegalArgumentException("Image cannot be null");
        }

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int frameWidth = gp.screenWidth;
        int frameHeight = gp.screenHeight;

        double widthRatio = (double) frameWidth / originalWidth;
        double heightRatio = (double) frameHeight / originalHeight;
        double scaleFactor = Math.max(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * scaleFactor);
        int newHeight = (int) (originalHeight * scaleFactor);

        if (dimension.equals("width")){
            return newWidth;
        }
        else if (dimension.equals("height")){
            return newHeight;
        }
        else {
            throw new IllegalArgumentException("Invalid dimension specs");
        }
    }
}
