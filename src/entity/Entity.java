package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
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

    public boolean knockBack = false;
    public int knockBackCounter = 0;

    public BufferedImage img1, img2, img3, img4, img5, img6;
    public Entity() {
    }

    public void update() {
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

    void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }//setzt den Spieler auf seine Startposition zurück(löschen, falls unnötig)
}

//TODO: Reevaluation der benutzten Variablen und der Notwendigkeit für das Spiel
