package Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {

    GamePanel gp;
    public boolean isRightClickHolding = false;
    public boolean leftClicked = false; // Reflects the state of the left mouse button
    private boolean leftButtonPressed = false; // Track if the left button is currently pressed

    private long lastAttackTime = 0;
    private static final long ATTACK_COOLDOWN = 500;
    private static final int CLICK_DURATION = 150; // Duration for which leftClicked is true

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // This can be left empty or used for additional logic if needed
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gp.gameState == gp.playState) {
            if (e.getButton() == MouseEvent.BUTTON1 && !isRightClickHolding) {
                long currentTime = System.currentTimeMillis();
                if (!leftButtonPressed && (currentTime - lastAttackTime >= ATTACK_COOLDOWN)) {
                    leftClicked = true; // Register the click
                    lastAttackTime = currentTime; // Update the last attack time
                    leftButtonPressed = true; // Mark the button as pressed

                    // Start a timer to reset leftClicked after CLICK_DURATION milliseconds
                    Timer timer = new Timer(CLICK_DURATION, event -> {
                        leftClicked = false; // Reset leftClicked after the duration
                    });
                    timer.setRepeats(false); // Only execute once
                    timer.start();
                }
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                isRightClickHolding = true;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gp.gameState == gp.playState) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                isRightClickHolding = false;
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftButtonPressed = false; // Reset the pressed state
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // This can be left empty or used for additional logic if needed
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // This can be left empty or used for additional logic if needed
    }
}