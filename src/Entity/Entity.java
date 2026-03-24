package Entity;

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
    int startY;
    char direction = 'U';
    boolean onGround = true;
    int velocityX = 0;
    int velocityY = 0;
    int speed;
    int freezeFrames = 0;


    Entity(Image image, int x, int y, int width, int height, int speed) {
        this.image = image;
        this.originalImage = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
        this.speed = speed;
    }

    void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }
}

//TODO: Reevaluation der benutzten Variablen und der Notwendigkeit für das Spiel