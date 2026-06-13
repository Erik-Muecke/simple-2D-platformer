package object;

import main.GamePanel;



public class OBJ_Spike extends SuperObject {

    public OBJ_Spike(GamePanel gp) {
        name = "Spike";
        collision = true;

        image = loadImage("/objects/spikes.png");
    }
}
