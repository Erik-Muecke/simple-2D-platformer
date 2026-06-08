package object;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import main.GamePanel;

public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int x, y;
    public Rectangle solidArea = new Rectangle(0, 0, 32 * GamePanel.scale, 32 * GamePanel.scale);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public int amount = 1;
    public boolean stackable = false;

    protected BufferedImage loadImage(String path) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("Ressource nicht gefunden: " + path);
            }
            return ImageIO.read(stream);
        } catch (Exception e) {
            System.err.println("Fehler beim Laden des Objektbildes: " + e.getMessage());
            return null;
        }
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        if (image == null) {
            return;
        }

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
