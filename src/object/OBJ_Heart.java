package object;

import main.GamePanel;


public class OBJ_Heart extends SuperObject {

    public OBJ_Heart(GamePanel gp) {
        name = "heart";

        image = imgLoader.scaleImage("/objects/heart_full.png", gp.tileSize / 2, gp.tileSize / 2);
    }
}