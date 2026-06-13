package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;

import entity.Entity;
import object.SuperObject;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    BufferedImage titleImage;
    BufferedImage heart_full, heart_half, heart_blank;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    public int slotCol = 0;
    public int slotRow = 0;
    public SuperObject selectedItem = null;

    public int commandNum = 0;

    public String currentDialogue = "";
    public int transitionCounter = 0;
    public int shopSlotRow = 0;
    public int activeNPCIndex = 999;
    public int tradeMenuState = 0; // 0=main, 1=buy, 2=sell

    // Fonts
    Font arial_40, arial_80B;

    public UI(GamePanel gp) {
        this.gp = gp;
        // initialize fonts
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        try {
            titleImage = ImageIO.read(getClass().getResourceAsStream("/player/kartoni.png"));

            heart_full = ImageIO.read(getClass().getResourceAsStream("/objects/heart_full.png"));
            heart_half = ImageIO.read(getClass().getResourceAsStream("/objects/heart_half.png"));
            heart_blank = ImageIO.read(getClass().getResourceAsStream("/objects/heart_blank.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }//the constructor

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
            return;
        }

        if(gp.gameOver) {
            drawPlayerLife();
            drawGameOver();
            return;
        }

        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if(gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessages();

            if(gp.gameOver) {
                drawGameOver();
            }
        }
        if(gp.gameState == gp.inventoryState) {
            drawInventory();
        }
        if(gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }

        if(gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }

        if(gp.gameState == gp.transitionState) {
            drawTransition();
        }

        if(gp.gameState == gp.tradeState) {
            drawTradeScreen();
        }
    }//the UI draw function

    private void drawMessages() {
        int messageX = 8;
        int messageY = gp.tileSize * 3 - 18;

        g2.setFont(new Font("Arial", Font.BOLD, 24));

        for(int i = 0; i < message.size(); i++) {
            if(message.get(i) != null) {
                g2.setColor(Color.BLACK);

                g2.drawString(
                        message.get(i),
                        messageX + 2,
                        messageY + 2
                );

                g2.setColor(Color.WHITE);

                g2.drawString(
                        message.get(i),
                        messageX,
                        messageY
                );

                int counter = messageCounter.get(i) + 1;

                messageCounter.set(i, counter);

                messageY += 40;

                if(messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public void drawTitleScreen() {

        // background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // draw character image
        int imgX = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
        int imgY =gp.tileSize * 5;

        g2.drawImage(titleImage, imgX, imgY, gp.tileSize * 2, gp.tileSize * 2, null);

        //title
        g2.setFont(arial_80B);

        String text = "ADVENTURE AWAITS";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        // shadow
        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);

        // main text
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //menu
        g2.setFont(arial_40);

        //new game
        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 5;
        g2.drawString(text, x, y);

        if(gp.keyHandler.commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }

        //load game
        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if(gp.keyHandler.commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }

        //quit
        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if(gp.keyHandler.commandNum == 2) {
            g2.drawString(">", x - 40, y);
        }

    }//draw the title screen

    public void drawPauseScreen() {

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);

    }//pause the game

    public void drawPlayerLife() {

        int x = gp.tileSize / 4;
        int y = gp.tileSize / 4;
        int i = 0;

        // max life
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, gp.tileSize, gp.tileSize, null);
            i++;
            x += gp.tileSize;
        }

        // reset position
        x = gp.tileSize / 4;
        y = gp.tileSize / 4;
        i = 0;

        // current lifes
        int life = gp.player.life;

        while (i < life) {

            g2.drawImage(heart_half, x, y, gp.tileSize, gp.tileSize, null);

            i++;

            if (i < life) {
                g2.drawImage(heart_full, x, y, gp.tileSize, gp.tileSize, null);
            }

            i++;
            x += gp.tileSize;
        }

        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.setColor(Color.BLUE);

        g2.drawString(
                "Mana: " + gp.player.mana + "/" + gp.player.maxMana,
                20,
                100
        );

        g2.setColor(Color.YELLOW);
        g2.drawString(
                "Coins: " + gp.player.hasCoin,
                20,
                135
        );
    }

    public void drawInventory() {
        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth  = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;

        int cols = 5;
        int rows = 4;
        int padding = 10;
        int slotSize = Math.min(
                (frameWidth  - padding * (cols + 1)) / cols,
                (frameHeight - padding * (rows + 1)) / rows
        );
        int gridW = cols * slotSize + padding * (cols - 1);
        int gridH = rows * slotSize + padding * (rows - 1);
        int slotX = frameX + (frameWidth  - gridW) / 2;
        int slotY = frameY + (frameHeight - gridH) / 2;

        // Background
        g2.setColor(new Color(40, 40, 40, 220));
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 10, 10);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(100, 100, 100));
        g2.drawRoundRect(frameX, frameY, frameWidth, frameHeight, 10, 10);

        // Slots
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int sx = slotX + col * (slotSize + padding);
                int sy = slotY + row * (slotSize + padding);

                g2.setColor(new Color(25, 25, 25, 200));
                g2.fillRoundRect(sx, sy, slotSize, slotSize, 4, 4);

                g2.setStroke(new BasicStroke(1f));
                g2.setColor(new Color(80, 80, 80));
                g2.drawRoundRect(sx, sy, slotSize, slotSize, 4, 4);
            }
        }

        // Items
        int itemIndex = 0;
        for (SuperObject item : gp.player.inventory) {
            int row = itemIndex / cols;
            int col = itemIndex % cols;
            if (row >= rows) break;

            int sx = slotX + col * (slotSize + padding);
            int sy = slotY + row * (slotSize + padding);

            // yellow background if selected
            if (item == selectedItem) {
                g2.setColor(new Color(255, 215, 0, 120));
                g2.fillRoundRect(sx, sy, slotSize, slotSize, 4, 4);
            }

            g2.drawImage(item.image, sx + 4, sy + 4, slotSize - 8, slotSize - 8, null);
            if(item.amount > 1) {
                g2.setColor(Color.WHITE);
                g2.drawString(
                        String.valueOf(item.amount),
                        sx + slotSize - 15,
                        sy + slotSize - 5
                );
            }
            itemIndex++;
        }

        // Cursor
        int cx = slotX + slotCol * (slotSize + padding);
        int cy = slotY + slotRow * (slotSize + padding);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.YELLOW);
        g2.drawRoundRect(cx, cy, slotSize, slotSize, 4, 4);
    }

    public SuperObject getSelectedItem() {
        int itemIndex = slotCol + slotRow * 5;
        if(itemIndex < gp.player.inventory.size()) {
            return gp.player.inventory.get(itemIndex);
        }

        return null;
    }

    public void drawTradeScreen() {
        switch (tradeMenuState) {
            case 0: drawTradeMainMenu(); break;
            case 1: drawBuyScreen();     break;
            case 2: drawSellScreen();    break;
        }
    }

    private void drawTradeMainMenu() {
        int panelX = gp.screenWidth  / 2 - 150;
        int panelY = gp.screenHeight / 2 - 120;
        int panelW = 300, panelH = 240;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String title = "Merchant";
        int titleX = panelX + (panelW - g2.getFontMetrics().stringWidth(title)) / 2;
        g2.drawString(title, titleX, panelY + 45);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(255, 215, 0));
        g2.drawString("Coins: " + gp.player.hasCoin, panelX + 15, panelY + 75);

        String[] options = {"Buy", "Sell", "Back"};
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        for (int i = 0; i < options.length; i++) {
            int optY = panelY + 115 + i * 45;
            if (i == commandNum) {
                g2.setColor(new Color(255, 200, 0, 180));
                g2.fillRoundRect(panelX + 20, optY - 22, panelW - 40, 32, 8, 8);
                g2.setColor(Color.BLACK);
            } else {
                g2.setColor(Color.WHITE);
            }
            int textX = panelX + (panelW - g2.getFontMetrics().stringWidth(options[i])) / 2;
            g2.drawString(options[i], textX, optY);
        }
    }

    private void drawBuyScreen() {
        if (activeNPCIndex == 999 || gp.npc[activeNPCIndex] == null) return;
        SuperObject[] shop = gp.npc[activeNPCIndex].shopInventory;

        int panelX = gp.screenWidth  / 2 - 180;
        int panelY = gp.screenHeight / 2 - 150;
        int panelW = 360, panelH = 300;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Buy", panelX + panelW / 2 - 20, panelY + 40);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(255, 215, 0));
        g2.drawString("Your coins: " + gp.player.hasCoin, panelX + 15, panelY + 70);

        for (int i = 0; i < shop.length; i++) {
            if (shop[i] == null) continue;
            int rowY = panelY + 105 + i * 55;
            if (i == shopSlotRow) {
                g2.setColor(new Color(255, 200, 0, 150));
                g2.fillRoundRect(panelX + 10, rowY - 20, panelW - 20, 40, 6, 6);
            }
            if (shop[i].image != null)
                g2.drawImage(shop[i].image, panelX + 15, rowY - 18, 32, 32, null);
            g2.setColor(Color.WHITE);
            g2.drawString(shop[i].name + " x" + shop[i].amount, panelX + 55, rowY);
            g2.setColor(new Color(255, 215, 0));
            g2.drawString(shop[i].price + " coins", panelX + panelW - 100, rowY);
        }

        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(Color.GRAY);
        g2.drawString("W/S select   ENTER buy   ESC back", panelX + 10, panelY + panelH - 10);
    }

    private void drawSellScreen() {
        int panelX = gp.screenWidth  / 2 - 180;
        int panelY = gp.screenHeight / 2 - 150;
        int panelW = 360, panelH = 300;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Sell", panelX + panelW / 2 - 22, panelY + 40);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(255, 215, 0));
        g2.drawString("Your coins: " + gp.player.hasCoin, panelX + 15, panelY + 70);

        if (gp.player.inventory.isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.drawString("Inventory is empty.", panelX + 60, panelY + 150);
        } else {
            for (int i = 0; i < gp.player.inventory.size(); i++) {
                SuperObject item = gp.player.inventory.get(i);
                int rowY = panelY + 105 + i * 55;
                if (i == shopSlotRow) {
                    g2.setColor(new Color(255, 200, 0, 150));
                    g2.fillRoundRect(panelX + 10, rowY - 20, panelW - 20, 40, 6, 6);
                }
                if (item.image != null)
                    g2.drawImage(item.image, panelX + 15, rowY - 18, 32, 32, null);
                g2.setColor(Color.WHITE);
                g2.drawString(item.name + (item.amount > 1 ? " x" + item.amount : ""), panelX + 55, rowY);
                g2.setColor(new Color(255, 215, 0));
                g2.drawString(Math.max(1, item.price / 2) + " coins", panelX + panelW - 100, rowY);
            }
        }

        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(Color.GRAY);
        g2.drawString("W/S select   ENTER sell   ESC back", panelX + 10, panelY + panelH - 10);
    }

    public void drawOptionsScreen() {

        // panel on the right half of the screen
        int frameWidth  = gp.screenWidth / 2;
        int frameHeight = gp.screenHeight - gp.tileSize * 2;
        int frameX      = gp.screenWidth - frameWidth - gp.tileSize / 2;
        int frameY      = gp.tileSize;

        // dark background panel
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 20, 20);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(frameX, frameY, frameWidth, frameHeight, 20, 20);

        int x = frameX + gp.tileSize;
        int y = frameY + gp.tileSize;

        // title
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.setColor(Color.WHITE);
        g2.drawString("OPTIONS", x, y);

        g2.setFont(new Font("Arial", Font.PLAIN, 22));

        // Full Screen
        y += gp.tileSize;
        g2.drawString("Full Screen", x, y);
        g2.drawRect(x + 180, y - 22, 22, 22);
        if (gp.fullScreenOn) {
            g2.fillRect(x + 184, y - 18, 14, 14);
        }
        if (commandNum == 0) {
            g2.drawString(">", x - 25, y);
        }

        // Music
        y += gp.tileSize;
        g2.drawString("Music", x, y);
        g2.drawRect(x + 180, y - 18, 90, 18);
        if (commandNum == 1) {
            g2.drawString(">", x - 25, y);
        }

        // Sound Effects
        y += gp.tileSize;
        g2.drawString("Sound Effects", x, y);
        g2.drawRect(x + 180, y - 18, 90, 18);
        if (commandNum == 2) {
            g2.drawString(">", x - 25, y);
        }

        // Reset game
        y += gp.tileSize;
        g2.drawString("Reset game", x, y);
        if (commandNum == 3) {
            g2.drawString(">", x - 25, y);
        }

        // Quit
        y += gp.tileSize;
        g2.drawString("Save Game", x, y);
        if(commandNum == 4) {
            g2.drawString(">", x - 25, y);
        }

        y += gp.tileSize;
        g2.drawString("Load Game", x, y);
        if(commandNum == 5) {
            g2.drawString(">", x - 25, y);
        }

        y += gp.tileSize;
        g2.drawString("Quit", x, y);
        if(commandNum == 6) {
            g2.drawString(">", x - 25, y);
        }

        y += gp.tileSize;
        g2.drawString("Back", x, y);
        if(commandNum == 7) {
            g2.drawString(">", x - 25, y);
        }
    }

    public void drawDialogueScreen() {

        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        g2.setColor(new Color(0,0,0,220));
        g2.fillRoundRect(x,y,width,height,35,35);

        g2.setColor(Color.WHITE);
        g2.drawRoundRect(x,y,width,height,35,35);

        g2.setFont(g2.getFont().deriveFont(28F));

        int textX = x + gp.tileSize;
        int textY = y + gp.tileSize;
        int maxWidth = width - gp.tileSize * 2;
        FontMetrics fm = g2.getFontMetrics();

        for (String line : currentDialogue.split("\n")) {
            String[] words = line.split(" ");
            StringBuilder currentLine = new StringBuilder();
            for (String word : words) {
                String test = currentLine.length() > 0 ? currentLine + " " + word : word;
                if (fm.stringWidth(test) > maxWidth) {
                    g2.drawString(currentLine.toString(), textX, textY);
                    textY += 40;
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine = new StringBuilder(test);
                }
            }
            if (currentLine.length() > 0) {
                g2.drawString(currentLine.toString(), textX, textY);
                textY += 40;
            }
        }
    }

    public void drawTransition() {

        transitionCounter++;

        g2.setColor(
                new Color(
                        0,
                        0,
                        0,
                        transitionCounter * 5));

        g2.fillRect(
                0,
                0,
                gp.screenWidth,
                gp.screenHeight);

        if(transitionCounter == 50) {

            transitionCounter = 0;

            gp.gameState = gp.playState;
        }
    }

    public void drawGameOver() {

        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        int x;
        int y;

        String text = "GAME OVER";
        g2.setFont(arial_80B);
        x = getXforCenteredText(text);
        y = gp.tileSize * 3;

        // shadow
        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);

        // main text
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        // menu
        g2.setFont(arial_40);

        text = "RETRY";
        x = getXforCenteredText(text);
        y += gp.tileSize * 5;
        g2.drawString(text, x, y);

        if(gp.keyHandler.commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if(gp.keyHandler.commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if(gp.keyHandler.commandNum == 2) {
            g2.drawString(">", x - 40, y);
        }
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }

    public void clearMessages() {
        message.clear();
        messageCounter.clear();
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }//calculating the exact centre of the frame
}
