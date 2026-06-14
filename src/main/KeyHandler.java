package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, jumpPressed, enterPressed; //Booleans um den Status der gedrückten Tasten zu verfolgen
    public boolean shotKeyPressed;
    public int commandNum = 0; //Variable um die aktuelle Auswahl im Menü zu verfolgen, z.B. im Hauptmenü oder im Pausenmenü

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {

        int key = e.getKeyCode(); //Gibt den Code der gedrückten Taste zurück


        // Je nach aktuellem Spielstatus werden unterschiedliche Tastenaktionen ausgeführt
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
                    // Laden des gespeicherten Spiels
                    gp.mapIndicator = gp.saveHndlr.loadLevel();
                    gp.player.life = gp.saveHndlr.loadLives();
                    gp.gameState = gp.playState;
                    commandNum = 0;

                }

                if(commandNum == 2) {
                    System.exit(0);
                }
            }
        }

        else if (gp.gameState == gp.playState) {

            //Wenn die Taste gedrückt wird, wird der zugehörige boolean auf true gesetzt, somit ist die Taste gerade im Betrieb
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
            if (key == KeyEvent.VK_F){
                shotKeyPressed = true;
            }

            //Wechseln in die PauseState, wenn die Escape-Taste gedrückt wird
            if (key == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.pauseState;
            }
        }

        // Logik für die Navigation im Pausenmenü
        else if (gp.gameState == gp.pauseState) {
            if (key == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;

            }

            if(key == KeyEvent.VK_W) {
                commandNum--;
                if(commandNum < 0) {
                    commandNum = 1;
                }
            }

            if(key == KeyEvent.VK_S) {
                commandNum++;
                if(commandNum > 1) {
                    commandNum = 0;
                }
            }

            if(key == KeyEvent.VK_ENTER) {
                if(commandNum == 0) {
                    gp.gameState = gp.playState;
                }

                if(commandNum == 1) {
                    System.exit(0);
                }
            }

        }

        // Logik für die Navigation im Game Over Menü
        else if (gp.gameState == gp.gameOver) {

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
                    gp.resetGame();
                }
                if (commandNum == 1) {
                    gp.resetGame();
                    gp.mapIndicator = gp.saveHndlr.loadLevel();
                    gp.player.life = gp.saveHndlr.loadLives();
                    gp.aSetter.updateObject();
                    gp.gameState = gp.playState;
                    commandNum = 0;

                }
                if(commandNum == 2) {
                    System.exit(0);
                }
            }
        }

        // Logik für die Navigation im Win Menü
        else if (gp.gameState == gp.winState) {

            if(key == KeyEvent.VK_W) {
                commandNum--;
                if(commandNum < 0) {
                    commandNum = 1;
                }
            }

            if(key == KeyEvent.VK_S) {
                commandNum++;
                if(commandNum > 1) {
                    commandNum = 0;
                }
            }

            if(key == KeyEvent.VK_ENTER) {
                if(commandNum == 0) {
                    gp.resetGame();
                }
                if (commandNum == 1) {
                    System.exit(0);
                }
            }

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
        if(key == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
    }
}
