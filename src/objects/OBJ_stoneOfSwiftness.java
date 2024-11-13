package objects;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_stoneOfSwiftness extends superObject{

    GamePanel gp;

    public OBJ_stoneOfSwiftness(GamePanel gp){

        this.gp = gp;

        name = "Stone of Swiftness";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/OBJ_StoneOfSwiftness.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
