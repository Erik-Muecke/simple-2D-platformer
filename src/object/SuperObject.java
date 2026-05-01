package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int x, y;

    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Only draw if visible on screen
        if (x + gp.tileSize > gp.camera.x &&
                x - gp.tileSize < gp.camera.x + gp.screenWidth &&
                y + gp.tileSize > gp.camera.y &&
                y - gp.tileSize < gp.camera.y + gp.screenHeight) {

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}