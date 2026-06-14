package object;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

import main.ImageLoader;

// SuperObject ist die Basisklasse für alle Objekte im Spiel
public class SuperObject {

    ImageLoader imgLoader = new ImageLoader(); // Neue Instanz von ImageLoader für das Laden von Bildern
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int x, y;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    // draw-Methode, um das Objekt auf dem Bildschirm zu zeichnen
    public void draw(Graphics2D g2, GamePanel gp) {
        // Berechnet die Bildschirmposition des Objekts basierend auf der Kameraposition
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Zeichnet nur, wenn das Objekt im sichtbaren Bereich der Kamera liegt
        if (x + gp.tileSize > gp.camera.x &&
                x - gp.tileSize < gp.camera.x + gp.screenWidth &&
                y + gp.tileSize > gp.camera.y &&
                y - gp.tileSize < gp.camera.y + gp.screenHeight) {

            g2.drawImage(image, screenX, screenY, null); // Zeichnet das Objekt an der berechneten Bildschirmposition
        }
    }
}
