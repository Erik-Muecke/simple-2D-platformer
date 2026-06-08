package entity;

import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Entity {
    private final GamePanel gp;
    public static final int TYPE_PLAYER = 0;
    public static final int TYPE_NPC = 1;
    public static final int TYPE_MONSTER = 2;

    public int type = TYPE_NPC;
    public int x;  //position auf der x-Achse
    public int y;  //position auf der Y-Achse
    public int width; //Weite
    public int height;  //Höhe
    public Image image;
    public boolean isDead = false;

    int startX;
    int startY;// Die Startpositionen
    public char direction = 'U'; //Richtung, in welche der Spieler schaut
    public char directionBeforeKnockBack;
    public boolean onGround = true;// schaut ob der spieler sich auf dem Boden befindet
    public int velocityX = 0;
    public int velocityY = 0;// Bewegungsrichtungen
    public int speed;
    public int freezeFrames = 0;
    public Rectangle solidArea;
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int attackWidth;
    public int attackHeight;
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;
    public boolean collisionOn = false;

    public String name;
    public int maxLife;
    public int life;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public int actionLockCounter;
    public int walkingCounter;

    public boolean knockBack = false;
    public int knockBackCounter = 0;

    public BufferedImage img1, img2, img3, img4, img5, img6;
    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public Entity() {
        this.gp = null;  // or remove this overload entirely
    }

    public void update() {
    }

    public void checkDrop() {
    }

    public void dropItem(SuperObject droppedItem) {
        for(int i = 0; i < gp.obj.length; i++) {
            if(gp.obj[i] == null) {
                gp.obj[i] = droppedItem;
                gp.obj[i].x = x;
                gp.obj[i].y = y;
                break;
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (image != null) {
            g2.drawImage(image, this.x, this.y, this.width, this.height, null); // nur wenn bild vorhanden
        }else{
            g2.setColor(Color.RED);  //setzt die Farbe des Graphics2D-Objekts auf Rot, damit die folgenden Zeichnungen in Rot erscheinen
            g2.fillRect(x, y, width, height);  //zeichnet ein gefülltes Rechteck an der Position (100, 100) mit der Breite und Höhe von tileSize (48x48 Pixel)
            // Kein dispose(): Swing verwaltet den Graphics-Kontext selbst.
        }
    }
    public BufferedImage setup(String imagePath) {
        String path = imagePath.endsWith(".png") ? imagePath : imagePath + ".png";

        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream != null) {
                return ImageIO.read(stream);
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden des Bildes: " + path);
        }

        try (InputStream stream = getClass().getResourceAsStream("/res/missing/image_not_found.png")) {
            if (stream != null) {
                return ImageIO.read(stream);
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden des Ersatzbildes: " + e.getMessage());
        }

        BufferedImage fallback = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fallback.createGraphics();
        g2.setColor(Color.MAGENTA);
        g2.fillRect(0, 0, fallback.getWidth(), fallback.getHeight());
        g2.dispose();
        return fallback;
    }

    protected void setImageAndSolidAreaFromBlackPixels(String imagePath) {
        BufferedImage sprite = setup(imagePath);
        image = sprite;

        int minX = sprite.getWidth();
        int minY = sprite.getHeight();
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < sprite.getHeight(); y++) {
            for (int x = 0; x < sprite.getWidth(); x++) {
                Color color = new Color(sprite.getRGB(x, y), true);
                if (color.getAlpha() > 0
                        && color.getRed() <= 40
                        && color.getGreen() <= 40
                        && color.getBlue() <= 40) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            solidArea = new Rectangle(0, 0, width, height);
        } else {
            int scaledX = (int) Math.round((double) minX * width / sprite.getWidth());
            int scaledY = (int) Math.round((double) minY * height / sprite.getHeight());
            int scaledRight = (int) Math.round((double) (maxX + 1) * width / sprite.getWidth());
            int scaledBottom = (int) Math.round((double) (maxY + 1) * height / sprite.getHeight());
            solidArea = new Rectangle(scaledX, scaledY, scaledRight - scaledX, scaledBottom - scaledY);
        }

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }//setzt den Spieler auf seine Startposition zurück(löschen, falls unnötig)
}

//TODO: Reevaluation der benutzten Variablen und der Notwendigkeit für das Spiel
