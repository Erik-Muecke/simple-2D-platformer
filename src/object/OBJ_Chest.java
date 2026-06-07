package object;

import main.GamePanel;

public class OBJ_Chest extends SuperObject {

    public OBJ_Chest(GamePanel gp) {
        name = "Chest";
        collision = true;

        image = imgLoader.scaleImage("/objects/chest.png", gp.tileSize, gp.tileSize);
    }
}