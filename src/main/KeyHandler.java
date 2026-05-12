package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, jumpPressed, enterPressed; //Booleans um den Status der gedrückten Tasten zu verfolgen
    public int commandNum = 0;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {

        int key = e.getKeyCode(); //Gibt den Code der gedrückten Taste zurück

        //Wenn die Taste gedrückt wird, wird der zugehörige boolean auf true gesetzt, somit ist die Taste gerade im Betrieb
        if(gp.gameState == gp.titleState) {
            if(key == KeyEvent.VK_W) {
                commandNum--;
                if(commandNum < 0) {
                    commandNum = 2;
                }
            }

            if(key == KeyEvent.VK_S) {
                commandNum++;
                if(commandNum > 2) {
                    commandNum = 0;
                }
            }

            if(key == KeyEvent.VK_ENTER) {
                if(commandNum == 0) {
                    gp.gameState = gp.playState;
                }

                if(commandNum == 1) {
                    // optional: load game
                }

                if(commandNum == 2) {
                    System.exit(0);
                }
            }
        }

        else if (gp.gameState == gp.playState) {

            if (key == KeyEvent.VK_W){
                upPressed = true;
            }
            if (key == KeyEvent.VK_S){
                downPressed = true;
            }
            if (key == KeyEvent.VK_A){
                leftPressed = true;
            }
            if (key == KeyEvent.VK_D){
                rightPressed = true;
            }
            if (key == KeyEvent.VK_SPACE){
                jumpPressed = true;
            }
            if (key == KeyEvent.VK_ENTER){
                enterPressed = true;
            }

            if (key == KeyEvent.VK_P) {
                gp.gameState = gp.pauseState;
            }
        }//moving possible, during playstate only

        else if (gp.gameState == gp.pauseState) {
            if (key == KeyEvent.VK_P) {
                gp.gameState = gp.playState;
            }//if paused, no movement possible
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {

        int key = e.getKeyCode(); //Gibt den Code der gedrückten Taste zurück

        //Wenn die Taste losgelassen wird, wird der zugehörige boolean auf false gesetzt, somit ist die Taste gerade nicht im Betrieb
        if (key == KeyEvent.VK_W) {
            upPressed = false;
        }

        if (key == KeyEvent.VK_S) {
            downPressed = false;
        }

        if (key == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (key == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (key == KeyEvent.VK_SPACE) {
            jumpPressed = false;
        }
        if(key == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
    }
}
