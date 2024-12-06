package Entity;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    GamePanel gp;

    public int worldX,worldY;
    public int defaultSpeed;

    public BufferedImage up, down, left, right,
                         up2, down2, left2, right2; // idle

    // running animations

    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle();
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    String dialogues[] = new String[20];
    int dialogueIndex = 0;
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public int type; // 0 = player, 1 = npc, 2 = mob

    // CHARACTER STATUS
    public int maxLife;
    public int life;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction(){}

    public void speak(){
        if (dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }

        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void update(){

        setAction();

        collisionOn = false;
        gp.cCheker.checkTile(this);
        gp.cCheker.checkObject(this,false);
        gp.cCheker.checkEntity(this, gp.npc);
        gp.cCheker.checkEntity(this, gp.mobs);
        boolean contactPlayer = gp.cCheker.checkPlayer(this);

        if(this.type == 2 && contactPlayer){
            if (!gp.player.invincible){
                gp.player.life -=1;
                gp.player.invincible = true;
            }
        }

        if (!collisionOn){
            switch (direction){
                case "up": worldY -= defaultSpeed; break;
                case "down": worldY += defaultSpeed; break;
                case "left": worldX -= defaultSpeed; break;
                case "right": worldX += defaultSpeed; break;
            }
        }

        spriteCounter++;
        if(spriteCounter > 12){
            if (spriteNum == 1){
                spriteNum = 2;
            }
            else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    // method to modularize the image setting code
    public BufferedImage setup(String path){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(path + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }


    public void draw(Graphics2D g2){
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (gp.player.screenX > gp.player.worldX){
            screenX = worldX;
        }
        if(gp.player.screenY > gp.player.worldY) {
            screenY = worldY;
        }
        int rightOffset = gp.screenWidth - gp.player.screenX;
        if (rightOffset > gp.worldWidth - gp.player.worldX) {
            screenX = gp.screenWidth - (gp.worldWidth - worldX);
        }
        int bottomOffset = gp.screenHeight - gp.player.screenY;
        if(bottomOffset > gp.worldHeight - gp.player.worldY) {
            screenY = gp.screenHeight - (gp.worldHeight - worldY);
        }

        switch (direction){
            case "up":
                if (spriteNum == 1){
                    image = up;
                }
                if (spriteNum == 2){
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1){
                    image = down;
                }
                if (spriteNum == 2){
                    image = down2;
                }
                break;
            case "right":
                if (spriteNum == 1){
                    image = right;
                }
                if (spriteNum == 2){
                    image = right2;
                }
                break;
            case "left":
                if (spriteNum == 1){
                    image = left;
                }
                if (spriteNum == 2){
                    image = left2;
                }
                break;
        }

//            // HIT BOX
//            g2.setColor(Color.red);
//            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        // if player is around edge, draw everything
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
        else if (gp.player.worldX < gp.player.screenX ||
                gp.player.worldY < gp.player.screenY ||
                rightOffset > gp.worldWidth - gp.player.worldX ||
                bottomOffset > gp.worldHeight - gp.player.worldY ) {
            g2.drawImage(image,screenX,screenY,gp.tileSize,gp.tileSize,null);
        }

    }
}