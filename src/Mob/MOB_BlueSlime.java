package Mob;

import Entity.Entity;
import Main.GamePanel;

import java.util.Random;

public class MOB_BlueSlime extends Entity {

    public MOB_BlueSlime(GamePanel gp) {
        super(gp);

        type = 2;
        name = "Blue Slime";
        defaultSpeed = 1;
        maxLife = 4;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();

    }

    public void getImage(){
        up = setup("/Mobs/blue_slime2");
        up2 = setup("/Mobs/blue_slime1");
        down = setup("/Mobs/blue_slime2");
        down2 = setup("/Mobs/blue_slime1");
        right = setup("/Mobs/blue_slime2");
        right2 = setup("/Mobs/blue_slime1");
        left = setup("/Mobs/blue_slime2");
        left2 = setup("/Mobs/blue_slime1");
    }

    public void setAction(){
        actionLockCounter ++;

        if (actionLockCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }
}
