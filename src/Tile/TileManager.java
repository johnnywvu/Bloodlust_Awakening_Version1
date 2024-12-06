package Tile;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp){
        this.gp = gp;

        tile = new Tile[60];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/tutorialLevel.txt");
    }

    public void getTileImage(){
        // 0, 1, 3, 4, 5, 6, 14, 15, 16, 17, 24, 26, 27, 28, 33, 40, 44, 45, 46, 49
        setup(0,"000", false);
        setup(1,"001", true);

        setup(3,"003", true);
        setup(4,"004", true);
        setup(5,"005", true);
        setup(6,"006", true);







        setup(14,"014", true);
        setup(15,"015", true);
        setup(16,"016", true);
        setup(17,"017", true);






        setup(24,"024", true);

        setup(26,"026", true);
        setup(27,"027", true);
        setup(28,"028", true);




        setup(33,"033", true);






        setup(40,"040", true);



        setup(44,"044", true);
        setup(45,"045", true);
        setup(46,"046", true);
        setup(49,"049", false);


        setup(52,"052", false);
        setup(53,"053",false);
    }

    public void setup(int index, String imageName, boolean collision){

        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){
        try{

            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0, row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow){

                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }

            }
            br.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int worldCol = 0, worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // stop moving camera at edge
            if (gp.player.screenX > gp.player.worldX){
                screenX = worldX;
            }
            if(gp.player.screenY > gp.player.worldY) {
                screenY = worldY;
            }
            int rightOffset = gp.screenWidth - gp.player.screenX;
            if (rightOffset > gp.worldWidth - gp.player.worldX) {
                screenX = gp.screenWidth - (gp.worldWidth - worldX);
            }
            int bottomOffset = gp.screenHeight - gp.player.screenY;
            if(bottomOffset > gp.worldHeight - gp.player.worldY) {
                screenY = gp.screenHeight - (gp.worldHeight - worldY);
            }

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)  {

                g2.drawImage(tile[tileNum].image,screenX,screenY,null);
            }
            else if(gp.player.screenX > gp.player.worldX || gp.player.screenY > gp.player.worldY||
                    rightOffset > gp.worldWidth - gp.player.worldX || bottomOffset > gp.worldHeight - gp.player.worldY)
            {
                g2.drawImage(tile[tileNum].image,screenX,screenY,null);
            }

            worldCol++;

            if (worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }

    }

}
