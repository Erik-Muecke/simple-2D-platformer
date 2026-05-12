package main;

import entity.Player;

public class Camera {

    public int x, y;

    public int screenWidth;
    public int screenHeight;
    public int worldWidth;
    public int worldHeight;

    public Camera(int screenWidth, int screenHeight, int worldWidth, int worldHeight) {
        //System.out.println("worldHeight: " + worldHeight + " screenHeight: " + screenHeight + " y: " + y); for debugging only
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void update(Player player) {
        // center on player
        x = player.x - screenWidth / 2;
        y = player.y - screenHeight / 2;

        // clamp to world bounds
        x = Math.max(0, Math.min(x, worldWidth - screenWidth));
        y = Math.max(0, Math.min(y, worldHeight - screenHeight));
    }
}
