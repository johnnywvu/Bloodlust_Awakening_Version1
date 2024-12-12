package objects;

import Entity.Entity;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_dungeonDoor extends Entity {
    
    public OBJ_dungeonDoor(GamePanel gp){
        super(gp);

        name = "Dungeon Door";
        down = setup("/objects/OBJ_lockedDungeonDoor", gp.tileSize, gp.tileSize);

        collision = true;
    }
}
