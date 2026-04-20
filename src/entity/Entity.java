package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int x;  //position auf der x-Achse
    public int y;  //position auf der Y-Achse
    int width; //Weite
    int height;  //Höhe
    Image image;
    boolean isDead = false;

    int startX;
    int startY;// Die Startpositionen
    char direction = 'U'; //Richtung, in welche der Spieler schaut
    boolean onGround = true;// schaut ob der spieler sich auf dem Boden befindet
    int velocityX = 0;
    int velocityY = 0;// Bewegungsrichtungen
    public int speed;
    int freezeFrames = 0;

    public BufferedImage img1, img2, img3, img4, img5, img6;
    public Entity() {

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
