package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import Main.GamePanel;
import Main.KeyHandler;
import Main.UI;

public class Player extends Entity{

    KeyHandler keyH;
    UI ui;

    public final int screenX;
    public final int screenY;

    // additional frames for smoother run cycle (PLAYER ONLY)
    BufferedImage    upRun1, upRun2, upRun3,
                    downRun1,downRun2, downRun3,
                    leftRun1, leftRun2, leftRun3,
                    rightRun1, rightRun2, rightRun3;

    public boolean isMoving = false;

    // 0 = default/null     1 = melee       2 = range       3= mage
    public int gearSelected = 0;

    //  DASH VARIABLES
    public boolean isDashing = false;
    int dashSpeed = 10; int dashTime = 100;
    private long dashStartTime, dashCooldown = 3000, dashEndTime = 0, currentTime;

    // DASH BAR VARIABLES
    public float dashBarMax = 100f;  // Maximum dash duration (100% capacity)
    private float dashBarCurrent = dashBarMax;  // Current fill of the dash bar

    public Player(GamePanel gp, KeyHandler keyH){

        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 28;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 28;
        solidArea.height = 20;

        setDefaultValues();
        getPlayerImage();
    }
    
    public void setDefaultValues(){
        worldX = gp.tileSize * 45;  // STARTING X POSITION
        worldY = gp.tileSize * 43;  // STARTING Y POSITION

        defaultSpeed = 3;
        direction = "down";

        // PLAYER STATS
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage(){
        up = setup("/player/back_idle");
        down = setup("/player/front_idle");
        right = setup("/player/right_idle");
        left = setup("/player/left_idle");

        // run cycle
        upRun1 = setup("/player/back_run1");
        upRun2 = setup("/player/back_run2");
        upRun3 = setup("/player/back_run3");

        downRun1 = setup("/player/front_run1");
        downRun2 = setup("/player/front_run2");
        downRun3 = setup("/player/front_run3");

        rightRun1 = setup("/player/right_running1");
        rightRun2 = setup("/player/right_running2");
        rightRun3 = setup("/player/right_running3");

        leftRun1 = setup("/player/left_running1");
        leftRun2 = setup("/player/left_running2");
        leftRun3 = setup("/player/left_running3");
    }

    public void update() {

        // SPACE PRESSED = DASH TRIGGERED
        if (keyH.spacePressed && !isDashing && System.currentTimeMillis() - dashEndTime >= dashCooldown && isMoving) {
            defaultSpeed = dashSpeed;
            isDashing = true;
            dashStartTime = System.currentTimeMillis();
        }

        // AFTER DASH IS TRIGGERED = disable isDashing
        if (isDashing){
            currentTime = System.currentTimeMillis();
            dashBarCurrent -= 15f;        // DECREASE DASH BAR
            if (currentTime - dashStartTime >= dashTime){
                isDashing = false;
                dashEndTime = currentTime;
                defaultSpeed = 3;
            }
        }

        // SLOWLY REFILL DASHBAR
        if(!isDashing && dashBarCurrent < dashBarMax){
            dashBarCurrent += 0.57f;
        }

        // SO DASHBAR DOESNT EXCEED LIMITS
        if (dashBarCurrent > dashBarMax) dashBarCurrent = dashBarMax;
        if (dashBarCurrent < 0) dashBarCurrent = 0;

        // UPDATE DASHBAR VALUES TO BE DRAWN TO UI CLASS
        gp.ui.updateDashBar(dashBarCurrent);

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed
            || keyH.enterPressed) {

            isMoving = true;

            // Determine the direction
            if (keyH.upPressed) {direction = "up";}
            else if (keyH.downPressed) {direction = "down";}
            else if (keyH.leftPressed) {direction = "left";}
            else if (keyH.rightPressed) {direction = "right";}

            // Check for tile collisions
            collisionOn = false;
            gp.cCheker.checkTile(this);

            // Check for object collisions
            int objIndex = gp.cCheker.checkObject(this, true);
            pickUpObject(objIndex);

            // check npc collisions
            int npcIndex = gp.cCheker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // check mob collisions
            int mobIndex = gp.cCheker.checkEntity(this, gp.mobs);
            contactMob(mobIndex);

            // check event
            gp.eHandler.checkEvent();

            // IF THERE IS NO COLLISION, MOVEMENT HAPPENS
            if (!collisionOn && !keyH.enterPressed) {

                // MOVEMENT
                switch (direction) {
                    case "up": worldY -= defaultSpeed; break;
                    case "down": worldY += defaultSpeed; break;
                    case "left": worldX -= defaultSpeed; break;
                    case "right": worldX += defaultSpeed;break;
                }
            }

            gp.keyH.enterPressed = false;

            // Sprite Replacer
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum++;
                if (spriteNum > 4) { // Assuming you have 4 frames for running
                    spriteNum = 1; // Reset to first running frame
                }
                spriteCounter = 0;
            }
        } else {
            // RESET SPRITE TO IDLE STANCE IF NOT MOVING
            spriteNum = 1;
            isMoving = false;
        }

        // PLAYER GEAR SELECTION
        if (keyH.onePressed) {gearSelected = 1;}
        if (keyH.twoPressed) {gearSelected = 2;}
        if (keyH.threePressed) {gearSelected = 3;}
        if (keyH.fourPressed) {gearSelected = 4;}

        // TRIGGERED WHEN TAKING DAMAGE TO AVOID CONTINUOUS DAMAGE
        if (invincible){
            invincibleCounter++;
            if (invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }
    
    // player character collides with object = event happens
    public void pickUpObject(int i) {

        if (i != 999) {

        }

    }

    // player character collision with npc
    public void interactNPC(int i){
        if (i != 999) {
            if(gp.keyH.enterPressed){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void contactMob(int i){
        if (i != 999){
            if (!invincible){
                life -=1;
                invincible = true;
            }
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            switch(direction) {
                case "up":
                    if (spriteNum == 1){image = upRun1;}
                    if (spriteNum == 2){image = upRun2;}
                    if (spriteNum == 3){image = upRun3;}
                    if (spriteNum == 4){image = upRun2;}
                    break;
                case "down":
                    if (spriteNum == 1){image = downRun1;}
                    if (spriteNum == 2){image = downRun2;}
                    if (spriteNum == 3){image = downRun3;}
                    if (spriteNum == 4){image = downRun2;}
                    break;
                case "right":
                    if (spriteNum == 1){image = rightRun1;}
                    if (spriteNum == 2){image = rightRun2;}
                    if (spriteNum == 3){image = rightRun3;}
                    if (spriteNum == 4){image = rightRun2;}
                    break;
                case "left":
                    if (spriteNum == 1){image = leftRun1;}
                    if (spriteNum == 2){image = leftRun2;}
                    if (spriteNum == 3){image = leftRun3;}
                    if (spriteNum == 4){image = leftRun2;}
                    break;
            }
        } else {
            switch (direction){
                case "up": image = up; break;
                case "down": image = down; break;
                case "right": image = right; break;
                case "left": image = left; break;
            }
        }
////        HIT BOX
//        g2.setColor(Color.red);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        // to stop camera from moving at edge of the screen
        int x = screenX;
        int y = screenY;

        if(screenX > worldX) {
            x = worldX;
        }
        if (screenY > worldY){
            y = worldY;
        }
        int rightOffset = gp.screenWidth - screenX;
        if (rightOffset > gp.worldWidth - worldX) {
            x = gp.screenWidth - (gp.worldWidth - worldX);
        }
        int bottomOffset = gp.screenHeight - screenY;
        if(bottomOffset > gp.worldHeight - worldY) {
            y = gp.screenHeight - (gp.worldHeight - worldY);
        }

        if (invincible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
        }

        g2.drawImage(image, x,y,null);

        // PLAYER INVINCIBLE MODE AFTER TAKING DAMAGE
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));

//        g2.setFont(new Font("Arial", Font.PLAIN, 26));
//        g2.setColor(Color.white);
//        g2.drawString("Invincible: " + invincibleCounter, 10, 400);
    }

}
