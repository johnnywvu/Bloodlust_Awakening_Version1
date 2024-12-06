package objects;

import Entity.Entity;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_dungeonKey extends Entity {

    public OBJ_dungeonKey(GamePanel gp) {
        super(gp);

        name = "Dungeon Key";
        down = setup("/objects/OBJ_dungeonKey");

    }
}
