package object;

import main.GamePanel;

public class OBJ_Coin extends SuperObject {

    public OBJ_Coin(GamePanel gp) {
        name = "Coin";
        collision = false;

        image = imgLoader.scaleImage("/objects/coin.png", gp.tileSize, gp.tileSize); //Laden des Bildes und Skalieren auf die Tilegröße
    }
}
