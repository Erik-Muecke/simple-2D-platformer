package object;

import main.GamePanel;

public class OBJ_Flag extends SuperObject {

    public OBJ_Flag(GamePanel gp) {
        name = "Flag";
        collision = false;

        image = imgLoader.scaleImage("/objects/flag.png", gp.tileSize, gp.tileSize);

    }
}