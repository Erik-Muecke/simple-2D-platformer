package main;

import object.*;
import monster.*;

import java.util.Arrays;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }


    public void setScene0() {

        gp.obj[6] = new OBJ_Coin(gp); //Erstellen eines neuen Objektes
        gp.obj[6].x = 13 * gp.tileSize; //Festlegen der x-Position des Objektes
        gp.obj[6].y = 4 * gp.tileSize; //Festlegen der y-Position des Objektes

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 31 * gp.tileSize;
        gp.obj[4].y = 3 * gp.tileSize;

        gp.obj[5] = new OBJ_Heart(gp);
        gp.obj[5].x = 36 * gp.tileSize;
        gp.obj[5].y = (3 * gp.tileSize) + 16; //16 wird addiert, um die Position des Herzens anzupassen, damit es nicht zu hoch in der Luft schwebt

        gp.obj[7] = new OBJ_SpeedBooster(gp);
        gp.obj[7].x = 7 * gp.tileSize;
        gp.obj[7].y = 15 * gp.tileSize;

        gp.monster[0] = new JumpSlime(gp); //Erstellen eines neuen Monsters
        gp.monster[0].x = 11 * gp.tileSize ; //Festlegen der x-Position des Objektes
        gp.monster[0].y = 15 * gp.tileSize; //Festlegen der y-Position des Objektes

        gp.monster[1] = new HeavyFlyer(gp);
        gp.monster[1].x = 19 * gp.tileSize ;
        gp.monster[1].y = 13 * gp.tileSize;
    }



    public void setScene1() {

        gp.obj[6] = new OBJ_Coin(gp);
        gp.obj[6].x = 36 * gp.tileSize;
        gp.obj[6].y = 4 * gp.tileSize;

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

        gp.obj[7] = new OBJ_SpeedBooster(gp);
        gp.obj[7].x = 18 * gp.tileSize;
        gp.obj[7].y = 15 * gp.tileSize;

    }

    public void setScene2() {

        gp.obj[6] = new OBJ_Coin(gp);
        gp.obj[6].x = 7 * gp.tileSize;
        gp.obj[6].y = 3 * gp.tileSize;

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 34 * gp.tileSize;
        gp.obj[4].y = 6 * gp.tileSize;

        gp.obj[5] = new OBJ_Heart(gp);
        gp.obj[5].x = 24 * gp.tileSize;
        gp.obj[5].y = (16 * gp.tileSize) + 16;

        gp.obj[7] = new OBJ_JumpBooster(gp);
        gp.obj[7].x = 15 * gp.tileSize;
        gp.obj[7].y = 12 * gp.tileSize;

        gp.monster[1] = new HeavyFlyer(gp);
        gp.monster[1].x = 18 * gp.tileSize ;
        gp.monster[1].y = 11 * gp.tileSize;
    }

    public void setScene3() {

        gp.obj[6] = new OBJ_Coin(gp);
        gp.obj[6].x = 14 * gp.tileSize;
        gp.obj[6].y = 2 * gp.tileSize;

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 35 * gp.tileSize;
        gp.obj[4].y = 11 * gp.tileSize;

        gp.obj[5] = new OBJ_Heart(gp);
        gp.obj[5].x = 34 * gp.tileSize;
        gp.obj[5].y = (6 * gp.tileSize) + 16;

        gp.monster[0] = new GreenSlime(gp);
        gp.monster[0].x = 9 * gp.tileSize ;
        gp.monster[0].y = 9 * gp.tileSize;

        gp.monster[1] = new GreenSlime(gp);
        gp.monster[1].x = 14 * gp.tileSize ;
        gp.monster[1].y = 9 * gp.tileSize;
    }

    public void setScene4() {

        gp.obj[0] = new OBJ_Key(gp); //Erstellen eines neuen Objektes und
        gp.obj[0].x = 16 * gp.tileSize; //Festlegen der x-Position des Objektes
        gp.obj[0].y = 4 * gp.tileSize; //Festlegen der y-Position des Objektes

        gp.obj[2] = new OBJ_Door(gp);
        gp.obj[2].x = 18 * gp.tileSize;
        gp.obj[2].y = 17 * gp.tileSize;

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 34 * gp.tileSize;
        gp.obj[4].y = 17 * gp.tileSize;

        gp.monster[1] = new HeavyFlyer(gp);
        gp.monster[1].x = 11 * gp.tileSize ;
        gp.monster[1].y = 16 * gp.tileSize;
    }

    public void setScene5() {

        gp.obj[6] = new OBJ_Coin(gp);
        gp.obj[6].x = 7 * gp.tileSize;
        gp.obj[6].y = 14 * gp.tileSize;

        gp.obj[4] = new OBJ_Flag(gp);
        gp.obj[4].x = 36 * gp.tileSize;
        gp.obj[4].y = 3 * gp.tileSize;

        gp.obj[5] = new OBJ_Heart(gp);
        gp.obj[5].x = 19 * gp.tileSize;
        gp.obj[5].y = (3 * gp.tileSize) + 16;

        gp.obj[7] = new OBJ_JumpBooster(gp);
        gp.obj[7].x = 30 * gp.tileSize;
        gp.obj[7].y = 7 * gp.tileSize;

        gp.monster[0] = new GreenSlime(gp);
        gp.monster[0].x = 8 * gp.tileSize ;
        gp.monster[0].y = 7 * gp.tileSize;

        gp.monster[1] = new HeavyFlyer(gp);
        gp.monster[1].x = 14 * gp.tileSize ;
        gp.monster[1].y = 12 * gp.tileSize;
    }


    public void updateScene() {
        Arrays.fill(gp.obj, null); // Alle Objekte im Array auf null setzen, um sicherzustellen, dass keine alten Objekte mehr vorhanden sind
        Arrays.fill(gp.monster, null); // Alle Monster im Array auf null setzen, um sicherzustellen, dass keine alten Monster mehr vorhanden sind
        switch(gp.mapIndicator){
            case 0:
                setScene0();
                break;
            case 1:
                setScene1();
                break;
            case 2:
                setScene2();
                break;
            case 3:
                setScene3();
                break;
            case 4:
                setScene4();
                break;
            case 5:
                setScene5();
                break;

        }
    }
}
