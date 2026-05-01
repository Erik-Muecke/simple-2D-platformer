package main;

import object.OBJ_Key;
import object.OBJ_Door;
import object.OBJ_Chest;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_Key();
        gp.obj[0].x = 10 * gp.tileSize;
        gp.obj[0].y = 7 * gp.tileSize;

        gp.obj[1] = new OBJ_Key();
        gp.obj[1].x = 5 * gp.tileSize;
        gp.obj[1].y = 4 * gp.tileSize;

        gp.obj[2] = new OBJ_Door();
        gp.obj[2].x = 14 * gp.tileSize;
        gp.obj[2].y = 2 * gp.tileSize;

        gp.obj[3] = new OBJ_Chest();
        gp.obj[3].x = 14 * gp.tileSize;
        gp.obj[3].y = 7 * gp.tileSize;
    }
}