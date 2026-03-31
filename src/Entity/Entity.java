package Entity;

import java.awt.*;

public class Entity {
    public int x;  //position auf der X-Achse
    public int y;  //position auf der Y-Achse
    int width; //Weite
    int height;  //Höhe
    Image image;
    Image originalImage;
    boolean isDead = false;

    int startX;
    int startY;// Die Startpositionen
    char direction = 'U'; //Richtung, in welche der Spieler schaut
    boolean onGround = true;// schaut ob der spieler sich auf dem Boden befindet
    int velocityX = 0;
    int velocityY = 0;// Bewegungsrichtungen
    public int speed;
    int freezeFrames = 0;


    public Entity(Image image, int x, int y, int width, int height, int speed) {
        this.image = image;//das anfangsbild
        this.originalImage = image;//zur möglichen zurücksetzung des Bildes
        this.x = x;
        this.y = y;//wo befindet sich der Spieler/Gegner definition
        this.width = width;
        this.height = height;//GRösse und weite des Spielers
        this.startX = x;
        this.startY = y;//Startposition
        this.speed = speed;//Geschwindigkeit, mit der sich der Spieler bewegt
    }
    public void draw(Graphics g) {
        if (this.image != null) {
            g.drawImage(this.image, this.x, this.y, this.width, this.height, null); // nur wenn bild vorhanden
        } else {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            g2.fillRect(x, y, width, height);
        }
    }
    void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }//setzt den Spieler auf seine Startposition zurück(löschen, falls unnötig)
}

//TODO: Reevaluation der benutzten Variablen und der Notwendigkeit für das Spiel
