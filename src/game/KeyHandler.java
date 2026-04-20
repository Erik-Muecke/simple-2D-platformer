package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    
    public boolean upPressed, downPressed, leftPressed, rightPressed; //Booleans um den Status der gedrückten Tasten zu verfolgen

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {

    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {

        int key = e.getKeyCode(); //Gibt den Code der gedrückten Taste zurück

        //Wenn die Taste gedrückt wird, wird der zugehörige boolean auf true gesetzt, somit ist die Taste gerade im Betrieb
        if (key == KeyEvent.VK_W) {
            upPressed = true;
        }

        if (key == KeyEvent.VK_S) {
            downPressed = true;
        }

        if (key == KeyEvent.VK_A) {
            leftPressed = true;
        }

        if (key == KeyEvent.VK_D) {
            rightPressed = true;
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
    }

}
