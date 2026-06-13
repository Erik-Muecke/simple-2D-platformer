package object;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import main.GamePanel;

public class SuperObject {

    public BufferedImage image;//creating an image for the objects
    public String name;//name of the object
    public boolean collision = false;//a variabel, to test for collision
    public int x, y;//position on the map
    public Rectangle solidArea = new Rectangle(0, 0, 32 * GamePanel.scale, 32 * GamePanel.scale);//creating a solid area
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public int amount = 1;//amount of one object at the merchant
    public boolean stackable = false;//looks, if an object can be stacked in the inventory
    public int price;//price of an object

    public SuperObject copy() {//used for the objects bought at the merchant
        SuperObject copy = new SuperObject();
        copy.name = this.name;
        copy.image = this.image;
        copy.price = this.price;
        copy.amount = 1;
        copy.stackable = this.stackable;
        copy.collision = this.collision;
        return copy;
    }

    protected BufferedImage loadImage(String path) {//getting the image for the object
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

    public void draw(Graphics2D g2, GamePanel gp) {//the draw function of the Objects
        if (image == null) {
            return;
        }

        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;//getting the coordinates, where the object should stand

        // Only draw if visible on screen
        if (x + gp.tileSize > gp.camera.x &&
                x - gp.tileSize < gp.camera.x + gp.screenWidth &&
                y + gp.tileSize > gp.camera.y &&
                y - gp.tileSize < gp.camera.y + gp.screenHeight) {

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
