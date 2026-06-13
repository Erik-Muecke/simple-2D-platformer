package main;

import object.*;
import monster.GreenSlime;

import java.util.Arrays;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setMonsterScene0() {

        gp.monster[0] = new GreenSlime(gp);
        gp.monster[0].x = 11 * gp.tileSize ;
        gp.monster[0].y = 15 * gp.tileSize;

        gp.monster[1] = new GreenSlime(gp);
        gp.monster[1].x = 19 * gp.tileSize ;
        gp.monster[1].y = 15 * gp.tileSize;
    }

    public void setMonsterScene3() {

        gp.monster[0] = new GreenSlime(gp);
        gp.monster[0].x = 9 * gp.tileSize ;
        gp.monster[0].y = 9 * gp.tileSize;

        gp.monster[1] = new GreenSlime(gp);
        gp.monster[1].x = 14 * gp.tileSize ;
        gp.monster[1].y = 9 * gp.tileSize;
    }

    public void setMonsterScene5() {

        gp.monster[0] = new GreenSlime(gp);
        gp.monster[0].x = 8 * gp.tileSize ;
        gp.monster[0].y = 7 * gp.tileSize;

    }

    public void setObjectScene0() {

        gp.obj[0] = new OBJ_Key(gp); //Erstellen eines neuen Objektes und
        gp.obj[0].x = 13 * gp.tileSize; //Festlegen der x-Position des Objektes
        gp.obj[0].y = 4 * gp.tileSize; //Festlegen der y-Position des Objektes

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 31 * gp.tileSize;
        gp.obj[4].y = 3 * gp.tileSize;

        gp.obj[5] = new OBJ_Heart(gp);
        gp.obj[5].x = 36 * gp.tileSize;
        gp.obj[5].y = (3 * gp.tileSize) + 16;
    }



    public void setObjectScene1() {

        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].x = 15 * gp.tileSize;
        gp.obj[0].y = 3 * gp.tileSize;

        gp.obj[2] = new OBJ_Door(gp);
        gp.obj[2].x = 16 * gp.tileSize;
        gp.obj[2].y = 15 * gp.tileSize;

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 34 * gp.tileSize;
        gp.obj[4].y = 15 * gp.tileSize;

        gp.obj[5] = new OBJ_Heart(gp);
        gp.obj[5].x = 18 * gp.tileSize;
        gp.obj[5].y = (7 * gp.tileSize) + 16;
    }

    public void setObjectScene2() {
        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 34 * gp.tileSize;
        gp.obj[4].y = 6 * gp.tileSize;

        gp.obj[5] = new OBJ_Heart(gp);
        gp.obj[5].x = 24 * gp.tileSize;
        gp.obj[5].y = (16 * gp.tileSize) + 16;
    }

    public void setObjectScene3() {

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 35 * gp.tileSize;
        gp.obj[4].y = 11 * gp.tileSize;

        gp.obj[5] = new OBJ_Heart(gp);
        gp.obj[5].x = 34 * gp.tileSize;
        gp.obj[5].y = (6 * gp.tileSize) + 16;
    }

    public void setObjectScene4() {


        gp.obj[0] = new OBJ_Key(gp); //Erstellen eines neuen Objektes und
        gp.obj[0].x = 16 * gp.tileSize; //Festlegen der x-Position des Objektes
        gp.obj[0].y = 4 * gp.tileSize; //Festlegen der y-Position des Objektes

        gp.obj[2] = new OBJ_Door(gp);
        gp.obj[2].x = 18 * gp.tileSize;
        gp.obj[2].y = 17 * gp.tileSize;

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 34 * gp.tileSize;
        gp.obj[4].y = 17 * gp.tileSize;
    }

    public void setObjectScene5() {
        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 36 * gp.tileSize;
        gp.obj[4].y = 3 * gp.tileSize;

        gp.obj[5] = new OBJ_Heart(gp);
        gp.obj[5].x = 19 * gp.tileSize;
        gp.obj[5].y = (3 * gp.tileSize) + 16;
    }


    public void updateObject() {
        Arrays.fill(gp.obj, null);
        Arrays.fill(gp.monster, null);
        switch(gp.mapIndicator){
            case 0:
                setObjectScene0();
                setMonsterScene0();
                break;
            case 1:
                setObjectScene1();
                break;
            case 2:
                setObjectScene2();
                break;
            case 3:
                setObjectScene3();
                setMonsterScene3();
                break;
            case 4:
                setObjectScene4();
                break;
            case 5:
                setObjectScene5();
                setMonsterScene5();
                break;

        }
    }
}
