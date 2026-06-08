package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import object.SuperObject;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, jumpPressed, enterPressed; //Booleans um den Status der gedrückten Tasten zu verfolgen
    public boolean shotKeyPressed;
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


        // ESCAPE öffnet/schließt das Options Menü - wird zuerst geprüft
        if (key == KeyEvent.VK_ESCAPE) {
            if (gp.gameState == gp.optionsState) {
                gp.gameState = gp.playState;    // options schließen
                gp.ui.commandNum = 0;
            } else if (gp.gameState == gp.playState) {
                gp.gameState = gp.optionsState; // options öffnen
                gp.ui.commandNum = 0;
            } else if (gp.gameState == gp.inventoryState) {
                gp.gameState = gp.playState;    // inventory schließen
            }
            return;
        }
        // Title screen input uses commandNum as the selected menu row.
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

            // When game over is visible, movement is disabled and ENTER controls the menu.
            if (gp.gameOver) {
                if (key == KeyEvent.VK_W) {
                    commandNum--;
                    if (commandNum < 0) {
                        commandNum = 1;
                    }
                }

                if (key == KeyEvent.VK_S) {
                    commandNum++;
                    if (commandNum > 1) {
                        commandNum = 0;
                    }
                }

                if (key == KeyEvent.VK_ENTER) {
                    if (commandNum == 0) {
                        gp.resetGame();
                    }
                    if (commandNum == 1) {
                        System.exit(0);
                    }
                }

                return;
            }
        }

        if (gp.gameState == gp.playState) {

            // Gameplay keys set flags; Player.update consumes those flags each frame.
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

            if (key == KeyEvent.VK_P) {
                gp.gameState = gp.pauseState;
            }

            if(key == KeyEvent.VK_I) {
                gp.gameState = gp.inventoryState;
            }
        }//moving possible, during playstate only

        else if (gp.gameState == gp.inventoryState) {
            // Inventory navigation changes the selected slot, not the player movement flags.
            if (key == KeyEvent.VK_I) {
                gp.gameState = gp.playState;
            }

            if (key == KeyEvent.VK_W) {
                gp.ui.slotRow--;
                if (gp.ui.slotRow < 0) {
                    gp.ui.slotRow = 3;
                }
            }
            if (key == KeyEvent.VK_S) {
                gp.ui.slotRow++;
                if (gp.ui.slotRow > 3) {
                    gp.ui.slotRow = 0;
                }
            }
            if (key == KeyEvent.VK_A) {
                gp.ui.slotCol--;
                if (gp.ui.slotCol < 0) {
                    gp.ui.slotCol = 4;
                }
            }
            if (key == KeyEvent.VK_D) {
                gp.ui.slotCol++;
                if (gp.ui.slotCol > 4) {
                    gp.ui.slotCol = 0;
                }
            }
            if (key == KeyEvent.VK_ENTER) {
                useSelectedInventoryItem();
            }

        } else if(gp.gameState == gp.optionsState) {

            if (key == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 5;
                }
            }

            if (key == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 5) {
                    gp.ui.commandNum = 0;
                }
            }
            if (key == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.toggleFullScreen();
                } else if (gp.ui.commandNum == 1) {
//                    gp.music.volumeScale++;
//                    if(gp.music.volumeScale > 5) {
//                        gp.music.volumeScale = 0;
//                    }
//
//                    gp.music.checkVolume();
                } else if (gp.ui.commandNum == 2) {
//                    gp.se.volumeScale++;
//                    if(gp.se.volumeScale > 5) {
//                        gp.se.volumeScale = 0;
//                    }
//                    gp.se.checkVolume();
                } else if (gp.ui.commandNum == 3) {
                    gp.resetGame();
                } else if (gp.ui.commandNum == 4) {
                    System.exit(0);
                } else if (gp.ui.commandNum == 5) {
                    gp.gameState = gp.playState;
                }
            }

            if (key == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;
            }
        }

        else if (gp.gameState == gp.pauseState) {
            if (key == KeyEvent.VK_P) {
                gp.gameState = gp.playState;
            }//if paused, no movement possible
        }

    }

    private void useSelectedInventoryItem() {
        // Only consumable items should have behavior here; keys are handled when opening doors.
        SuperObject item = gp.ui.getSelectedItem();
        if (item != null && item.name.equals("Healing Potion")) {
            gp.player.life = Math.min(gp.player.life + 4, gp.player.maxLife);
            gp.player.removeItemFromInventory("Healing Potion");
            gp.ui.addMessage("Recovered 4 HP!");
        } else if (item != null && item.name.equals("Mana Potion")) {
            gp.player.mana = Math.min(gp.player.mana + 2, gp.player.maxMana);
            gp.player.removeItemFromInventory("Mana Potion");
            gp.ui.addMessage("Recovered 2 Mana!");
        } else {
        gp.ui.selectedItem = item; // highlight it
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
        if (key == KeyEvent.VK_F11) {
            gp.toggleFullScreen();
        }
    }
}
