package main;

import java.awt.*;
import java.awt.image.BufferedImage;


public class UI {

    GamePanel gp;
    Graphics2D g2;
    BufferedImage titleImage;
    BufferedImage heart_full, heart_half, heart_blank;

    ImageLoader imgLoader = new  ImageLoader();
    // Fonts
    Font arial_40, arial_80B;

    public UI(GamePanel gp) {
        this.gp = gp;
        // initialize fonts
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

            titleImage = imgLoader.scaleImage("/player/kartoni.png", gp.tileSize * 2, gp.tileSize * 2);

            heart_full = imgLoader.scaleImage("/objects/heart_full.png", gp.tileSize, gp.tileSize);
            heart_half = imgLoader.scaleImage("/objects/heart_half.png", gp.tileSize, gp.tileSize);
            heart_blank = imgLoader.scaleImage(("/objects/heart_blank.png"), gp.tileSize, gp.tileSize);

    }//the constructor

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        if(gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }

        if(gp.gameState == gp.playState) {
            drawPlayerLife();
        }
        if(gp.gameState == gp.gameOver) {
            drawPlayerLife();
            drawGameOver();
        }

        if(gp.gameState == gp.winState) {
            drawWinScreen();
        }
    }//the UI draw function

    public void drawTitleScreen() {

        // background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // draw character image
        int imgX = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
        int imgY =gp.tileSize * 5;

        g2.drawImage(titleImage, imgX, imgY, null);

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

        //title
        g2.setFont(arial_80B);

        String text = "PAUSED";
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
        text = "CONTINUE";
        x = getXforCenteredText(text);
        y += gp.tileSize * 5;
        g2.drawString(text, x, y);

        if(gp.keyHandler.commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }

        //load game
        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if(gp.keyHandler.commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }

    }//pause the game

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

        text = "LOAD SAVE";
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

    public void drawWinScreen() {

        // background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // draw character image
        int imgX = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
        int imgY = gp.tileSize * 5;

        g2.drawImage(titleImage, imgX, imgY, null);

        //title
        g2.setFont(arial_80B);

        String text = "YOU WON!";
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

        text = "This is everything. You now can quit. Or you play it again.";
        x = getXforCenteredText(text);
        y += gp.tileSize * 5;
        g2.drawString(text, x, y);

        //new game
        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);

        if (gp.keyHandler.commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }

        //quit
        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if (gp.keyHandler.commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }
    }


    public void drawPlayerLife() {

        int x = gp.tileSize / 4;
        int y = gp.tileSize / 4;
        int i = 0;

        // max life
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
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

            g2.drawImage(heart_half, x, y, null);

            i++;

            if (i < life) {
                g2.drawImage(heart_full, x, y, null);
            }

            i++;
            x += gp.tileSize;
        }
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }//calculating the exact centre of the frame
}
