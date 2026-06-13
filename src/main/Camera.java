package main;

import entity.Player;

public class Camera {

    public int x, y; // camera offset in world space (top-left of screen)

    public int screenWidth; // visible screen width in pixels
    public int screenHeight; // visible screen height in pixels
    public int worldWidth; // total world width in pixels
    public int worldHeight; // total world height in pixels

    public Camera(int screenWidth, int screenHeight, int worldWidth, int worldHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void update(Player player) {

        // camera follows player by centering them on screen
        x = player.x - screenWidth / 2;
        y = player.y - screenHeight / 2;

        // prevent camera from going outside world boundaries
        x = Math.max(0, Math.min(x, worldWidth - screenWidth));
        y = Math.max(0, Math.min(y, worldHeight - screenHeight));
    }
}