package objects;

import Entity.Entity;
import Main.GamePanel;

public class OBJ_Melee extends Entity {

    public OBJ_Melee(GamePanel gp) {
        super(gp);

        name = "Echo of the Fallen";
        down = setup("/objects/sword", gp.tileSize, gp.tileSize);
        attackValue = 1;
    }
}
