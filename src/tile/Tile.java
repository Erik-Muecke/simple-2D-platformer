package tile;


import java.awt.image.BufferedImage;
public class Tile{
    public BufferedImage image; //das Bild der Kachel
    public boolean collision = false; //gibt an, ob die Kachel eine Kollision hat oder nicht
    public String name; //name der Kachel, für Kollisionsberechnung relevant

}
