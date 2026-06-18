package object;

import main.GamePanel;


public class OBJ_JumpBooster extends SuperObject {

    public OBJ_JumpBooster(GamePanel gp) {
        name = "JumpBooster";
        collision = false;

        image = imgLoader.scaleImage("/objects/JumpBooster.png", gp.tileSize, gp.tileSize); //Laden des Bildes und Skalieren auf die Tilegröße
    }
}
