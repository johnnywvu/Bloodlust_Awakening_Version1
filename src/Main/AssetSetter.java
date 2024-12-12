package Main;

import Entity.NPC_dungeonSlave;
import Mob.MOB_BlueSlime;
import objects.*;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject(){

        gp.obj[0] = new OBJ_dungeonKey(gp);
        gp.obj[0].worldX = 12 * gp.tileSize;
        gp.obj[0].worldY = 34 * gp.tileSize;

    }

    public void setNPC(){

        gp.npc[0] = new NPC_dungeonSlave(gp);
        gp.npc[0].worldX = 42 * gp.tileSize;
        gp.npc[0].worldY = 43 * gp.tileSize;

    }

    public void setMonster(){

        gp.mobs[0] = new MOB_BlueSlime(gp);
        gp.mobs[0].worldX = 43 * gp.tileSize;
        gp.mobs[0].worldY = 45 * gp.tileSize;

        gp.mobs[1] = new MOB_BlueSlime(gp);
        gp.mobs[1].worldX = 38 * gp.tileSize;
        gp.mobs[1].worldY = 48 * gp.tileSize;

        gp.mobs[2] = new MOB_BlueSlime(gp);
        gp.mobs[2].worldX = 37 * gp.tileSize;
        gp.mobs[2].worldY = 43 * gp.tileSize;

        gp.mobs[3] = new MOB_BlueSlime(gp);
        gp.mobs[3].worldX = 36 * gp.tileSize;
        gp.mobs[3].worldY = 46 * gp.tileSize;
//
        gp.mobs[4] = new MOB_BlueSlime(gp);
        gp.mobs[4].worldX = 39 * gp.tileSize;
        gp.mobs[4].worldY = 44 * gp.tileSize;

        gp.mobs[5] = new MOB_BlueSlime(gp);
        gp.mobs[5].worldX = 46 * gp.tileSize;
        gp.mobs[5].worldY = 43 * gp.tileSize;

    }
}
