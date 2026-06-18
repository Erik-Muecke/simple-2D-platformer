package object;

import main.GamePanel;

public class OBJ_JumpBoost extends SuperObject {

    public OBJ_JumpBoost(GamePanel gp) {
        name = "JumpBoost";
        collision = false;

        image = imgLoader.scaleImage("/objects/JumpBoost.png", gp.tileSize, gp.tileSize); //Laden des Bildes und Skalieren auf die Tilegröße

    }
}
