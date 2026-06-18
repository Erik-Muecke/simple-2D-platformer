package object;

import main.GamePanel;

public class OBJ_SpeedBooster extends SuperObject {

    public OBJ_SpeedBooster(GamePanel gp) {
        name = "SpeedBooster";
        collision = false;

        image = imgLoader.scaleImage("/objects/SpeedBooster.png", gp.tileSize, gp.tileSize); //Laden des Bildes und Skalieren auf die Tilegröße
    }
}
