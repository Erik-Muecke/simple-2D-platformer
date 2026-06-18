package object;

import main.GamePanel;

public class OBJ_SpeedBoost extends SuperObject {

    public OBJ_SpeedBoost(GamePanel gp) {
        name = "SpeedBoost";
        collision = false;

        image = imgLoader.scaleImage("/objects/SpeedBoost.png", gp.tileSize, gp.tileSize); //Laden des Bildes und Skalieren auf die Tilegröße
    }
}
