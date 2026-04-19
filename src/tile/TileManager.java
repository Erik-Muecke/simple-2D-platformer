package tile;

import main.GamePanel;

public class TileManager {

    GamePanel gp;
    Tile[] tile; //Array um die verschiedenen Kacheln zu speichern

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];

        getTileImage();
    }

    public void getTileImage() {
        //Hier werden die Bilder für die Kacheln geladen und die Kollisionseigenschaft festgelegt
        tile[0] = new Tile();
        tile[0].image = null; //Hier sollte das Bild für die Kachel geladen werden
        tile[0].collision = false;

        tile[1] = new Tile();
        tile[1].image = null; //Hier sollte das Bild für die Kachel geladen werden
        tile[1].collision = true;
    }
}
