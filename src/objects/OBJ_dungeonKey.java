package objects;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_dungeonKey extends superObject{

    GamePanel gp;

    public OBJ_dungeonKey(GamePanel gp) {

        this.gp = gp;

        name = "Dungeon Key";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/OBJ_dungeonKey.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
