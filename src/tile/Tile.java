package tile;

import entity.Entity;

import java.awt.image.BufferedImage;
public class Tile{
    public BufferedImage image;
    public boolean collision = false; //gibt an, ob die Kachel eine Kollision hat oder nicht
    public String name = "unknown";
    public int TileSize;
    public int width;
    public int height;
}
