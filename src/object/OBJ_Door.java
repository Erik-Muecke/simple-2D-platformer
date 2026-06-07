package object;

import main.GamePanel;



public class OBJ_Door extends SuperObject {

    public OBJ_Door(GamePanel gp) {
        name = "Door";
        collision = true;

        image = imgLoader.scaleImage("/objects/door.png", gp.tileSize, gp.tileSize);
    }
}