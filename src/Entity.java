package src;

import java.awt.*;

public class Entity {
    int x;  //position auf der X-Achse
    int y;  //position auf der Y-Achse
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
    int speed;
    int freezeFrames = 0;


    Entity(Image image, int x, int y, int width, int height, int speed) {
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

    void updateVelocity() {// die Funktion von oben/ setzt die aktive Velocity fest, hängt von der Direction ab
            if (this.direction == 'L'){
                this.velocityX = -speed;
                //this.velocityY = 0; //Wenn die direction Links ist, dann bewegt man sich nach links               
            }
            else if (this.direction == 'R'){
                this.velocityX = speed;
                //this.velocityY = 0; //Wenn die direction Rechts ist, dann bewegt man sich nach rechts                
            }
        }

    void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }//setzt den Spieler auf seine Startposition zurück(löschen, falls unnötig)
}

//TODO: Reevaluation der benutzten Variablen und der Notwendigkeit für das Spiel
