package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entity.Entity;
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

        if (gp.gameOver) {
            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;
            jumpPressed = false;
            enterPressed = false;
            shotKeyPressed = false;

            if (key == KeyEvent.VK_W) {
                commandNum--;
                if (commandNum < 0) {
                    commandNum = 2;
                }
            }

            if (key == KeyEvent.VK_S) {
                commandNum++;
                if (commandNum > 2) {
                    commandNum = 0;
                }
            }

            if (key == KeyEvent.VK_ENTER) {
                if (commandNum == 0) {
                    gp.resetGame();
                } else if (commandNum == 1) {
                    gp.saveLoad.load();
                    gp.gameOver = false;
                    gp.gameState = gp.playState;
                    commandNum = 0;
                } else if (commandNum == 2) {
                    System.exit(0);
                }
            }

            return;
        }

        // ESCAPE öffnet/schließt das Options Menü - wird zuerst geprüft
        if (key == KeyEvent.VK_ESCAPE) {
            if (gp.gameState == gp.optionsState) {
                gp.gameState = gp.playState;
                gp.ui.commandNum = 0;
            } else if (gp.gameState == gp.playState) {
                gp.gameState = gp.optionsState;
                gp.ui.commandNum = 0;
            } else if (gp.gameState == gp.inventoryState) {
                gp.gameState = gp.playState;
            } else if (gp.gameState == gp.dialogueState) {
                gp.gameState = gp.playState;
                gp.ui.activeNPCIndex = 999;
            } else if (gp.gameState == gp.tradeState) {
                if (gp.ui.tradeMenuState == 0) {
                    gp.gameState = gp.playState;
                    gp.ui.activeNPCIndex = 999;
                } else {
                    gp.ui.tradeMenuState = 0;
                    gp.ui.commandNum = 0;
                }
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
                    gp.saveLoad.load();
                    gp.gameState = gp.playState;
                }

                if(commandNum == 2) {
                    System.exit(0);
                }
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
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 7;
            }
            if (key == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 7) gp.ui.commandNum = 0;
            }

            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 7;
            }

            if (gp.ui.commandNum > 7) {
                gp.ui.commandNum = 0;
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
                        gp.saveLoad.save();
                } else if (gp.ui.commandNum == 5) {
                        gp.saveLoad.load();
                } else if (gp.ui.commandNum == 6) {
                        System.exit(0);
                } else if (gp.ui.commandNum == 7){
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
        } else if(gp.gameState == gp.dialogueState) {
            if (key == KeyEvent.VK_ENTER) {
                Entity npc = (gp.ui.activeNPCIndex != 999) ? gp.npc[gp.ui.activeNPCIndex] : null;
                boolean morelines = npc != null
                        && npc.dialogueIndex < npc.dialogues.length
                        && npc.dialogues[npc.dialogueIndex] != null
                        && !npc.dialogues[npc.dialogueIndex].isEmpty();
                if (morelines) {
                    gp.ui.currentDialogue = npc.dialogues[npc.dialogueIndex];
                    npc.dialogueIndex++;
                } else {
                    if (gp.npc[gp.ui.activeNPCIndex] instanceof entity.NPC_Merchant) {
                        gp.ui.tradeMenuState = 0;
                        gp.ui.commandNum = 0;
                        gp.gameState = gp.tradeState;
                    } else {
                        gp.gameState = gp.playState;
                        gp.ui.activeNPCIndex = 999;
                    }
                }
            }
            if (key == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;
            }
        }
        else if(gp.gameState == gp.tradeState) {
            if (gp.ui.tradeMenuState == 0) {
                if (key == KeyEvent.VK_W) { gp.ui.commandNum--; if (gp.ui.commandNum < 0) gp.ui.commandNum = 2; }
                if (key == KeyEvent.VK_S) { gp.ui.commandNum++; if (gp.ui.commandNum > 2) gp.ui.commandNum = 0; }
                if (key == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) { gp.ui.tradeMenuState = 1; gp.ui.shopSlotRow = 0; }
                    if (gp.ui.commandNum == 1) { gp.ui.tradeMenuState = 2; gp.ui.shopSlotRow = 0; }
                    if (gp.ui.commandNum == 2) { gp.gameState = gp.playState; }
                }
                if (key == KeyEvent.VK_ESCAPE) { gp.gameState = gp.playState; }
            } else if (gp.ui.tradeMenuState == 1) {
                if (key == KeyEvent.VK_W) { gp.ui.shopSlotRow--; if (gp.ui.shopSlotRow < 0) gp.ui.shopSlotRow = 1; }
                if (key == KeyEvent.VK_S) { gp.ui.shopSlotRow++; if (gp.ui.shopSlotRow > 1) gp.ui.shopSlotRow = 0; }
                if (key == KeyEvent.VK_ENTER) { buyItem(); }
                if (key == KeyEvent.VK_ESCAPE) { gp.ui.tradeMenuState = 0; gp.ui.commandNum = 0; }
            } else if (gp.ui.tradeMenuState == 2) {
                int maxSlot = Math.max(0, gp.player.inventory.size() - 1);
                if (key == KeyEvent.VK_W) { gp.ui.shopSlotRow--; if (gp.ui.shopSlotRow < 0) gp.ui.shopSlotRow = maxSlot; }
                if (key == KeyEvent.VK_S) { gp.ui.shopSlotRow++; if (gp.ui.shopSlotRow > maxSlot) gp.ui.shopSlotRow = 0; }
                if (key == KeyEvent.VK_ENTER) { sellItem(); }
                if (key == KeyEvent.VK_ESCAPE) { gp.ui.tradeMenuState = 0; gp.ui.commandNum = 0; }
            }
        }
    }

    public void buyItem() {
        if (gp.ui.activeNPCIndex == 999) return;
        SuperObject item = gp.npc[gp.ui.activeNPCIndex].shopInventory[gp.ui.shopSlotRow];
        if (item == null) return;
        if (gp.player.hasCoin >= item.price) {
            gp.player.hasCoin -= item.price;
            item.amount--;
            gp.player.addItemToInventory(item.copy());
            gp.ui.addMessage("Bought: " + item.name);
            if (item.amount <= 0) {
                gp.npc[gp.ui.activeNPCIndex].shopInventory[gp.ui.shopSlotRow] = null;
            }
        } else {
            gp.ui.addMessage("Not enough coins!");
        }
    }

    public void sellItem() {
        if (gp.player.inventory.isEmpty()) { gp.ui.addMessage("Nothing to sell!"); return; }
        int idx = gp.ui.shopSlotRow;
        if (idx >= gp.player.inventory.size()) return;
        SuperObject item = gp.player.inventory.get(idx);
        int sellPrice = Math.max(1, item.price / 2);
        gp.player.hasCoin += sellPrice;
        gp.player.removeItemFromInventory(item.name);
        gp.ui.addMessage("Sold " + item.name + " for " + sellPrice + " coins");
        if (gp.ui.shopSlotRow >= gp.player.inventory.size())
            gp.ui.shopSlotRow = Math.max(0, gp.player.inventory.size() - 1);
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
