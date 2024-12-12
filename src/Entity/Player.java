package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Main.GamePanel;
import Main.KeyHandler;
import Main.MouseHandler;
import Main.UI;

public class Player extends Entity{

    KeyHandler keyH;
    MouseHandler mouseH;
    private ArrayList<Integer> gearSelectionHistory;

    public final int screenX;
    public final int screenY;

    // additional frames for smoother run cycle (PLAYER ONLY)
    BufferedImage    upRun1, upRun2, upRun3,
            downRun1,downRun2, downRun3,
            leftRun1, leftRun2, leftRun3,
            rightRun1, rightRun2, rightRun3;

    public boolean isMoving = false;
    public boolean isAttacking = false;
    public boolean isDashing = false;
    public int gearSelected = 0;

    //  DASH VARIABLES
    int dashSpeed = 10;
    int dashTime = 100;
    private long dashStartTime, dashCooldown = 3000, dashEndTime = 0, currentTime;

    // DASH BAR VARIABLES
    public float dashBarMax = 100f;  // Maximum dash duration (100% capacity)
    private float dashBarCurrent = dashBarMax;  // Current fill of the dash bar

    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseH){

        super(gp);
        this.keyH = keyH;
        this.mouseH = mouseH;

        gearSelectionHistory  = new ArrayList<>();

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 28;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 28;
        solidArea.height = 20;

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getImage();
        getAttackImage();
        getShieldImage();
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

    public void getImage(){
        up = setup("/player/back_idle",gp.tileSize, gp.tileSize);
        down = setup("/player/front_idle",gp.tileSize, gp.tileSize);
        right = setup("/player/right_idle",gp.tileSize, gp.tileSize);
        left = setup("/player/left_idle",gp.tileSize, gp.tileSize);

        // run cycle
        upRun1 = setup("/player/back_run1",gp.tileSize, gp.tileSize);
        upRun2 = setup("/player/back_run2",gp.tileSize, gp.tileSize);
        upRun3 = setup("/player/back_run3",gp.tileSize, gp.tileSize);

        downRun1 = setup("/player/front_run1",gp.tileSize, gp.tileSize);
        downRun2 = setup("/player/front_run2",gp.tileSize, gp.tileSize);
        downRun3 = setup("/player/front_run3",gp.tileSize, gp.tileSize);

        rightRun1 = setup("/player/right_running1",gp.tileSize, gp.tileSize);
        rightRun2 = setup("/player/right_running2",gp.tileSize, gp.tileSize);
        rightRun3 = setup("/player/right_running3",gp.tileSize, gp.tileSize);

        leftRun1 = setup("/player/left_running1",gp.tileSize, gp.tileSize);
        leftRun2 = setup("/player/left_running2",gp.tileSize, gp.tileSize);
        leftRun3 = setup("/player/left_running3",gp.tileSize, gp.tileSize);
    }
    public void getAttackImage(){
        attackUp = setup("/player/attack-up",gp.tileSize, gp.tileSize + 11);
        attackDown = setup("/player/attack-down",gp.tileSize, gp.tileSize + 11);
        attackRight = setup("/player/attack-right", gp.tileSize+11, gp.tileSize);
        attackLeft = setup("/player/attack-left", gp.tileSize+11, gp.tileSize);
    }
    public void getShieldImage(){
        shieldUp = setup("/player/shield-back", gp.tileSize, gp.tileSize);
        shieldDown = setup("/player/shield-front", gp.tileSize, gp.tileSize);
        shieldRight = setup("/player/shield-right", gp.tileSize, gp.tileSize);
        shieldLeft = setup("/player/shield-left", gp.tileSize, gp.tileSize);
    }

    public void update() {
        dash();

        if (isAttacking){
            attacking();
        }
        else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {

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

        // GEAR SELECT
        if (!mouseH.isRightClickHolding){
            guarding = false;
            if (gearSelectionHistory.isEmpty()){ gearSelected = 0;}
            else { gearSelected = gearSelectionHistory.get(gearSelectionHistory.size() - 1); }

            if (keyH.onePressed) {gearSelected = 1;}
            if (keyH.twoPressed) {gearSelected = 2;}
            if (keyH.threePressed) {gearSelected = 3;}

            if (gearSelectionHistory.size() > 3){gearSelectionHistory.remove(0);}

            if (gearSelectionHistory.isEmpty() || gearSelected != gearSelectionHistory.get(gearSelectionHistory.size() - 1)){
                gearSelectionHistory.add(gearSelected);}
        } else {gearSelected = 4; guarding = true;}

//        if (guarding){
//            System.out.println("GUARDING");
//        } else {
//            System.out.println("NOT GUARDING");
//        }

        // TRIGGERED WHEN TAKING DAMAGE TO AVOID CONTINUOUS DAMAGE
        if (invincible){
            invincibleCounter++;
            if (invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (mouseH.leftClicked){
            isAttacking = true;
        } else {
            isAttacking = false;
        }

        if (isAttacking){
            System.out.println("ATTACKING");
        } else {
            System.out.println("NOT ATTACKING");
        }
    }

    public void attacking(){
        spriteCounter++;

        if (gearSelected == 1){
            if (spriteCounter >= 0 && spriteCounter <= 15){
                spriteNum = 1;

                int currentWorldX = worldX;
                int currentWorldY = worldY;
                int solidAreaWidth = solidArea.width;
                int solidAreaHeight = solidArea.height;

                switch (direction) {
                    case "up": worldY -= attackArea.height; break;
                    case "down": worldY += attackArea.height; break;
                    case "left": worldX -= attackArea.width; break;
                    case "right": worldX += attackArea.width; break;
                }
                solidArea.width = attackArea.width;
                solidArea.height = attackArea.height;

                int monsterIndex = gp.cCheker.checkEntity(this, gp.mobs);

                worldX = currentWorldX;
                worldY = currentWorldY;
                solidArea.width = solidAreaWidth;
                solidArea.height = solidAreaHeight;
                damageMonster(monsterIndex);

            }
            if (spriteCounter > 15){
                spriteNum = 2;
                spriteCounter = 0;
                isAttacking = false;
            }
        }else if (gearSelected == 2){           // RANGE
            // ADD LATER
        } else if (gearSelected == 3){          // MAGE
            // ADD LATER
        }
    }

    public void dash(){
        // SPACE PRESSED = DASH TRIGGERED
        if (keyH.spacePressed && !isDashing && System.currentTimeMillis() - dashEndTime >= dashCooldown && isMoving && !isAttacking) {
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

    public void damageMonster(int i){
        if (i != 999){

            if(!gp.mobs[i].invincible){
                gp.mobs[i].life -= 1;
                gp.mobs[i].invincible = true;

                if (gp.mobs[i].life <= 0){
                    gp.mobs[i] = null;
                }
            }

        }
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;
        // to stop camera from moving at edge of the screen
        int x = screenX;
        int y = screenY;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

            switch(direction) {
                case "up":
                    if (!isAttacking){
                        if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed) {image = up;}  else { // Idle up sprite}
                            if (spriteNum == 1){image = upRun1;}
                            if (spriteNum == 2){image = upRun2;}
                            if (spriteNum == 3){image = upRun3;}
                            if (spriteNum == 4){image = upRun2;}}
                    }
                    if (isAttacking){
                        switch (gearSelected){
                            case 1:
                                tempScreenY = screenY - 11;
                                if (spriteNum == 1) {image = attackUp;}
                                if (spriteNum == 2) {image = attackUp;}
                                break;
                        }
                    }
                        break;
                case "down":
                    if (!isAttacking){
                        if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed) {image = down;}else {  // Idle up sprite}
                            if (spriteNum == 1){image = downRun1;}
                            if (spriteNum == 2){image = downRun2;}
                            if (spriteNum == 3){image = downRun3;}
                            if (spriteNum == 4){image = downRun2;}}
                    }
                    if (isAttacking){
                        switch (gearSelected){
                            case 1:
                                if (spriteNum == 1) {image = attackDown;}
                                if (spriteNum == 2) {image = attackDown;}
                                break;
                        }
                    }
                        break;
                case "right":
                    if (!isAttacking){
                        if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed) {image = right;}else {  // Idle up sprite}
                            if (spriteNum == 1){image = rightRun1;}
                            if (spriteNum == 2){image = rightRun2;}
                            if (spriteNum == 3){image = rightRun3;}
                            if (spriteNum == 4){image = rightRun2;}}
                    }
                    if (isAttacking){
                        switch (gearSelected){
                            case 1:
                                if (spriteNum == 1) {image = attackRight;}
                                if (spriteNum == 2) {image = attackRight;}
                                break;
                        }
                    }
                        break;
                case "left":
                    if (!isAttacking){
                        if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed) {image = left;}else {  // Idle up sprite}
                            if (spriteNum == 1){image = leftRun1;}
                            if (spriteNum == 2){image = leftRun2;}
                            if (spriteNum == 3){image = leftRun3;}
                            if (spriteNum == 4){image = leftRun2;}}
                    }
                    if (isAttacking){
                        switch (gearSelected){
                            case 1:
                                tempScreenX = screenX - 11;
                                if (spriteNum == 1) {image = attackLeft;}
                                if (spriteNum == 2) {image = attackLeft;}
                                break;
                        }
                    }
                        break;
            }


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

        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(tempScreenX, tempScreenY, attackArea.width, attackArea.height);


//        //        HIT BOX
//        g2.setColor(Color.red);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

//        g2.setFont(new Font("Arial", Font.PLAIN, 26));
//        g2.setColor(Color.white);
//        g2.drawString("Invincible: " + invincibleCounter, 10, 400);
    }

}