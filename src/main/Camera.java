package main;

import entity.Player;

public class Camera {

    public int x, y;

    public int screenWidth;
    public int screenHeight;
    public int worldWidth;
    public int worldHeight;

    public Camera(int screenWidth, int screenHeight, int worldWidth, int worldHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void update(Player player) {
        // Kamera wird auf den Spieler zentriert
        x = player.x - screenWidth / 2;
        y = player.y - screenHeight / 2;

        // Kamera stoppt, damit sie nicht über die Weltgrenzen hinausgeht
        x = Math.max(0, Math.min(x, worldWidth - screenWidth)); // Verhindert, dass die Kamera über die linke oder rechte Grenze hinausgeht
        y = Math.max(0, Math.min(y, worldHeight - screenHeight)); // Verhindert, dass die Kamera über die obere oder untere Grenze hinausgeht
    }
}
