package main;

import object.OBJ_Flag;
import object.OBJ_Key;
import object.OBJ_Door;
import object.OBJ_Chest;
import object.OBJ_Flag;
import monster.GreenSlime;

import java.util.Arrays;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObjectScene0() {

        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].x = 10 * gp.tileSize;
        gp.obj[0].y = 4 * gp.tileSize;

        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].x = 0 * gp.tileSize;
        gp.obj[1].y = 0 * gp.tileSize;

        gp.obj[2] = new OBJ_Door(gp);
        gp.obj[2].x = 15 * gp.tileSize;
        gp.obj[2].y = 10 * gp.tileSize;

        gp.obj[3] = new OBJ_Chest(gp);
        gp.obj[3].x = 20 * gp.tileSize;
        gp.obj[3].y = 10 * gp.tileSize;

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 25 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;
    }//just actually placing all the objects

    public void setMonster() {

        gp.monster[0] = new GreenSlime(gp);
        gp.monster[0].x = gp.tileSize * 7;
        gp.monster[0].y = gp.tileSize * 10;

        gp.monster[1] = new GreenSlime(gp);
        gp.monster[1].x = gp.tileSize * 8;
        gp.monster[1].y = gp.tileSize * 3;
    }

    public void setObjectScene1() {
        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 25 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;
    }

    public void setObjectScene2() {
        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 25 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;
    }

    public void setObjectScene3() {
        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 25 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;
    }

    public void setObjectScene4() {
        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 25 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;
    }

    public void setObjectScene5() {
        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 25 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;
    }


    public void updateObject() {
        Arrays.fill(gp.obj, null);
        switch(gp.mapIndicator){
            case 0:
                setObjectScene0();
                break;
            case 1:
                setObjectScene1();
                break;
            case 2:
                setObjectScene2();
                break;
            case 3:
                setObjectScene3();
                break;

        }
    }
}
