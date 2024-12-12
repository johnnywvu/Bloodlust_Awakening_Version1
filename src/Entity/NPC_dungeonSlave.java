package Entity;

import Main.GamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_dungeonSlave extends Entity{

    public NPC_dungeonSlave(GamePanel gp){
        super(gp);

        direction = "down";
        defaultSpeed = 1;
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 32;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 8;

        getImage();
        setDialogue();
    }

    public void getImage(){
        up = setup("/NPC/dungeon_slave", gp.tileSize, gp.tileSize);
        down = setup("/NPC/dungeon_slave", gp.tileSize, gp.tileSize);
        right = setup("/NPC/dungeon_slave", gp.tileSize, gp.tileSize);
        left = setup("/NPC/dungeon_slave", gp.tileSize, gp.tileSize);
    }

    public void setDialogue(){
        dialogues[0] = "Ah... another soul, wandering\nthrough this accursed place.";
        dialogues[1] = " Tell me, do you tread here by choice...\n or by the cruel hand of fate?";
        dialogues[2] = "No matter. They all come... \nand they all fall.";
//        dialogues[3] = "I don't think, therefore I am not.";
//        dialogues[4] = "I don't think, therefore I am not.";
    }

    @Override
    public void update(){

        setAction();

        collisionOn = false;
        gp.cCheker.checkTile(this);
        gp.cCheker.checkObject(this,false);
        gp.cCheker.checkPlayer(this);

    }
    // NPC AI
    public void setAction(){

        actionLockCounter ++;
        if(actionLockCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100) + 1; // pick num 1-100
            if (i <= 25){
                direction = "up";
            }
            if (i > 25 && i <= 50){
                direction = "down";
            }
            if (i > 50 && i <= 75){
                direction = "left";
            }
            if (i > 74 && i <= 100){
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }

    public void speak(){
        super.speak();
    }

}
