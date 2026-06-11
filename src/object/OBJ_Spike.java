package object;

import main.GamePanel;



public class OBJ_Spike extends SuperObject {

    public OBJ_Spike(GamePanel gp) {
        name = "Door";
        collision = true;

        image = imgLoader.scaleImage("/objects/spikes.png", gp.tileSize, gp.tileSize);
    }
}