package tile;

import entity.Entity;

import java.awt.image.BufferedImage;
public class Tile extends Entity {
    public BufferedImage image;
    public boolean collision = false; //gibt an, ob die Kachel eine Kollision hat oder nicht
    public int TileSize;
    public int width;
    public int height;
}
