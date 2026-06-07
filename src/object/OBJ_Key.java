package object;

import main.GamePanel;


public class OBJ_Key extends SuperObject {

    public OBJ_Key(GamePanel gp) {
        name = "Key";

        image = imgLoader.scaleImage("/objects/key.png", gp.tileSize, gp.tileSize);
    }
}
