package Main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    // Screen Settings
    final int originalTileSize = 32; // 32 tile sprites
    final int scale = 3;

    final int tileSize = originalTileSize * scale; // 96
    final int maxScreenCol = 16;
    final int maxScreenWid = 9;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenWid;

    //FPS
    int fps = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    //  Set Player default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;


    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        // FUCK sleep method
        double drawInterval = 1000000000/fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update(){

        //  ONE DIRECTIONAL MOVEMENT
        if (keyH.upPressed){
            playerY -= playerSpeed;
        }
        else if (keyH.downPressed){
            playerY += playerSpeed;
        }
        else if (keyH.leftPressed){
            playerX -= playerSpeed;
        }
        else if (keyH.rightPressed){
            playerX += playerSpeed;
        }

        //  DIAGONAL MOVEMENT
        if (keyH.upPressed && keyH.rightPressed){ // up-right
            playerY -= playerSpeed * 0.5;
            playerX += playerSpeed * 0.5;
        }
        else if (keyH.upPressed && keyH.leftPressed){ // up-left
            playerY -= playerSpeed * 0.5;
            playerX -= playerSpeed * 0.5;
        }
        else if (keyH.downPressed && keyH.rightPressed){ // down-right
            playerY += playerSpeed * 0.5;
            playerX += playerSpeed * 0.5;
        }
        else if (keyH.downPressed && keyH.leftPressed){ // down-right
            playerY += playerSpeed * 0.5;
            playerX -= playerSpeed * 0.5;
        }

    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.red);

        g2.fillRect(playerX,playerY,tileSize,tileSize);

        g2.dispose();
    }
}
