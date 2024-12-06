package Main;

import java.awt.*;

public class EventHandler {

    GamePanel gp;
    EventRect eventRect[][];

    public int previousEventX, previousEventY;
    boolean canTouchEvent = true;


    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow){
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == gp.maxWorldCol){
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent(){

        //check if player is a tile away from last event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.tileSize){
            canTouchEvent = true;
        }

        if(canTouchEvent){
            if (hit(39,48,"down")){damagePit(39,48,gp.dialogueState);}
            if (hit(43,48,"any")){healingPool(43,48,gp.dialogueState);}
        }
    }

    public boolean hit (int col, int row, String reqDirection){
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        if (gp.player.solidArea.intersects(eventRect[col][row])){
            if (gp.player.direction.equals(reqDirection) || reqDirection.contentEquals("any")){
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }
 
    public void damagePit(int col, int row, int gameState){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fell into a pit.";
        gp.player.life -= 1;
        canTouchEvent = false;
    }

    public void healingPool(int col, int row,int gameState){
        if (gp.keyH.enterPressed) {
            gp.gameState = gameState;
            gp.ui.currentDialogue =
                   "You drank from the quarry's waters. " +
                   "A surge\nof divine energy floods your veins— " +
                   "the\nblood of angels courses through you." +
                   "Their\nblessing is now yours to bear.";
            gp.player.life = gp.player.maxLife;
        }
    }

}
