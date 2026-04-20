package game;

import game.entity.Entity;

import java.awt.*;

public class Player extends Entity { // Player erbt alle Eigenschaften und Methoden von Entity, da er eine spezielle Art von Entity ist, die sich bewegen kann und von der Spielfigur gesteuert wird.


    public Player(Image image, int x, int y, int width, int height, int speed) {
        //TODO: später weitere Variablen und Sprites für Animationen hinzufügen
        super(image, x, y, width, height, speed); //ruft den Konstruktor der übergeordneten Klasse Entity auf, um die Eigenschaften des Players zu initialisieren
    }
}
