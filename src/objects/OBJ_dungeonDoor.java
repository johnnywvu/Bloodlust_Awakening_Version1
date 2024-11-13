package objects;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_dungeonDoor extends superObject{

    GamePanel gp;

    public OBJ_dungeonDoor(GamePanel gp){

        this.gp = gp;

        name = "Dungeon Door";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/OBJ_lockedDungeonDoor.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        collision = true;
    }
}
