package objects;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_regularChest extends superObject{

    GamePanel gp;

    public OBJ_regularChest(GamePanel gp) {

        this.gp = gp;

        name = "regularChest";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/OBJ_regChest.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
