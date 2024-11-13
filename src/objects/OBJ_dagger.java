package objects;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_dagger extends superObject{

    GamePanel gp;

    public OBJ_dagger(GamePanel gp) {

        this.gp = gp;

        name = "Dagger";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/OBJ_dagger.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
