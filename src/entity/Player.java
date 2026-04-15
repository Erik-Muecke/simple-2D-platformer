package entity;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    private int jumpStrength =30;
    private int gravity =2;
    private int maxFallSpeed =12;


    public Player( GamePanel gp, KeyHandler keyH) {
        super();
        speed = 4; //Geschwindigkeit des Spielers, wie viele Pixel er sich pro Update bewegen soll
        width = 32*GamePanel.scale; //Breite des Spielers in Pixeln
        height = 32*GamePanel.scale; //Höhe des Spielers in Pixeln
        x = 100;
        y = gp.screenHeight;
        this.gp = gp;
        this.keyH = keyH;
        this.image = loadPlayerImage();
    }

    private static Image loadPlayerImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(Player.class.getResource("/player/kartoni.png"))); //nicht res/player/kartoni, da der res ordner im kompilierem out ordner nicht benutzt wird
        } catch (IOException | NullPointerException e) {
            System.err.println("Fehler beim Laden von kartoni.png: " + e.getMessage());
            return null;
        }
    }
    
    public void update(){
        //Hier werden die Spielobjekte aktualisiert, z.B. Positionen, Kollisionen, etc.
        if(keyH.leftPressed){
            x -= speed; //bewegt den Spieler nach links, indem die x-Position um die Geschwindigkeit des Spielers verringert wird
        }
        if(keyH.rightPressed){
            x += speed; //bewegt den Spieler nach rechts, indem die x-Position um die Geschwindigkeit des Spielers erhöht wird
        }

        if (keyH.jumpPressed && onGround) { // Überprüft ob am Boden und ob Sprungtaste gedrückt
            velocityY = -jumpStrength; // Geschwindigkeit wird mit der negativen Sprungkraft gleihgesetzt, damit später die Y Koordinate abnehmen kann
            onGround = false; //nicht mehr auf dem Boden
        }

        velocityY += gravity; //Geschwindigkeit wird um den Wert der Schwerkraft erhöht höherer Wert -> schnelleres Fallen
        if (velocityY > maxFallSpeed) velocityY = maxFallSpeed; //Fallgeschwindigkeit wird gedeckelt, damit man nicht "unendlich" schnell fällt
        y += velocityY; //Y position wird aktualisiert, da die aktuelle geschwindigkeit in Y Richtung entweder addiert oder subtrahiert wird, je nachdem was größer ist.

        int groundY = gp.getPreferredSize().height - height; // berechnet den y wert des bodens, wenn nicht vorhanden würde man ewig nach unten fallen
        if (y >= groundY) { //überprüft ob Player y wert auf oder unter dem boden liegt, wenn ja,
            y = groundY; //wird die Position dem Boden gleichgesetzt
            velocityY =0; //Die Geschwindigkeit auf 0
            onGround = true; // und die variable onGround auf true gesetzt, damit man erst nachdem man wieder unten ist erneut springen kann.
        }
    }
}
