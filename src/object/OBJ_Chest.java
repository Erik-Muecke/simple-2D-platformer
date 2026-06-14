package object;

import main.GamePanel;

public class OBJ_Chest extends SuperObject {

    public OBJ_Chest(GamePanel gp) {
        name = "Chest"; // Name des Objektes
        collision = true; // Kollision aktiviert, damit der Spieler nicht durch die Truhe laufen kann

        image = imgLoader.scaleImage("/objects/chest.png", gp.tileSize, gp.tileSize); //Laden des Bildes und Skalieren auf die Tilegröße
    }
}