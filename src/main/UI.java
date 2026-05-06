package main;

import java.awt.*;
import entity.Player;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
    }

    public void drawTitleScreen() {

        String text = "START GAME";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        g2.drawString(text, x, y);

        text = "PRESS ENTER";
        x = getXforCenteredText(text);
        y += gp.tileSize * 4;

        g2.drawString(text, x, y);
    }

    public void drawPauseScreen() {

        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public int getXforCenteredText(String text) {

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}