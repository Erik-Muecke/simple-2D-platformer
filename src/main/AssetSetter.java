package main; // belongs to main game package (core setup logic)

import entity.NPC_Backpacker;
import entity.NPC_Merchant;
import monster.*;
import object.*;

import java.util.Arrays; // used to quickly clear arrays (fill with null)

public class AssetSetter {
    GamePanel gp; // reference to main game panel (access to world, player, arrays)

    public AssetSetter(GamePanel gp) {
        this.gp = gp; // store reference so scenes can be built
    }

    public void setScene0() {

        gp.obj[0] = new OBJ_Key(); // key item spawn
        gp.obj[0].x = 10 * gp.tileSize; // position in world grid (tile-based)
        gp.obj[0].y = 2 * gp.tileSize;

        gp.obj[1] = new OBJ_HealingPotion();
        gp.obj[1].x = 0 * gp.tileSize;
        gp.obj[1].y = 0 * gp.tileSize;

        gp.obj[2] = new OBJ_Door(); // door that blocks progression
        gp.obj[2].x = 15 * gp.tileSize;
        gp.obj[2].y = 10 * gp.tileSize;

        gp.obj[3] = new OBJ_Chest(); // chest containing rewards
        gp.obj[3].x = 20 * gp.tileSize;
        gp.obj[3].y = 10 * gp.tileSize;

        gp.obj[4] = new OBJ_Heart(); // health pickup
        gp.obj[4].x = 14 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;

        gp.obj[5] = new OBJ_ManaPotion();
        gp.obj[5].x = 16 * gp.tileSize;
        gp.obj[5].y = 10 * gp.tileSize;

        gp.obj[6] = new OBJ_Flag(); // level end / goal
        gp.obj[6].x = 25 * gp.tileSize;
        gp.obj[6].y = 10 * gp.tileSize;

        gp.monster[0] = new HoverFlyer(gp); // flying enemy type
        gp.monster[0].x = gp.tileSize * 10;
        gp.monster[0].y = gp.tileSize * 5;

        gp.monster[1] = new FireSlime(gp);
        gp.monster[1].x = gp.tileSize * 7;
        gp.monster[1].y = gp.tileSize * 10;

        gp.monster[2] = new SpeedSlime(gp);
        gp.monster[2].x = gp.tileSize * 5;
        gp.monster[2].y = gp.tileSize * 10;

        gp.monster[3] = new HeavyFlyer(gp);
        gp.monster[3].x = gp.tileSize * 6;
        gp.monster[3].y = gp.tileSize * 10;

        gp.monster[4] = new FireShooter(gp);
        gp.monster[4].x = gp.tileSize * 5;
        gp.monster[4].y = gp.tileSize * 3;

        gp.monster[5] = new FireShooter(gp);
        gp.monster[5].x = gp.tileSize * 11;
        gp.monster[5].y = gp.tileSize * 3;
    }

    public void setScene1() {
        gp.obj[0] = new OBJ_Key(); // starting key
        gp.obj[0].x = 0 * gp.tileSize;
        gp.obj[0].y = 1 * gp.tileSize;

        gp.obj[1] = new OBJ_Door(); // exit door
        gp.obj[1].x = 16 * gp.tileSize;
        gp.obj[1].y = 1 * gp.tileSize;

        gp.obj[2] = new OBJ_SpeedBoost(); // movement upgrade
        gp.obj[2].x = 15 * gp.tileSize;
        gp.obj[2].y = 1 * gp.tileSize;

        gp.obj[3] = new OBJ_JumpBoost(); // jump upgrade
        gp.obj[3].x = 17 * gp.tileSize;
        gp.obj[3].y = 1 * gp.tileSize;

        gp.obj[4] = new OBJ_Flag(); // level end marker
        gp.obj[4].x = 25 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;

        gp.monster[0] = new JumpSlime(gp);
        gp.monster[0].x = gp.tileSize * 1;
        gp.monster[0].y = gp.tileSize * 7;

        gp.monster[1] = new FireSlime(gp);
        gp.monster[1].x = gp.tileSize * 3;
        gp.monster[1].y = gp.tileSize * 8;

        gp.monster[2] = new FireSlime(gp);
        gp.monster[2].x = gp.tileSize * 6;
        gp.monster[2].y = gp.tileSize * 5;

        gp.monster[3] = new FireSlime(gp);
        gp.monster[3].x = gp.tileSize * 11;
        gp.monster[3].y = gp.tileSize * 5;

        gp.npc[0] = new NPC_Backpacker(gp); // NPC tutorial/backstory
        gp.npc[0].x = gp.tileSize * 15;
        gp.npc[0].y = gp.tileSize * 1;

        gp.npc[1] = new NPC_Backpacker(gp);
        gp.npc[1].x = gp.tileSize * 17;
        gp.npc[1].y = gp.tileSize * 1;
        gp.npc[1].dialogues[0] = "You found me!"; // custom dialogue override
        gp.npc[1].dialogues[1] = "You will find the merchant in the next level, waiting for you just before the boss.";
        gp.npc[1].dialogues[2] = "I hope you will win!";
    }

    public void setScene2() {

        gp.obj[0] = new OBJ_SpecialKey();
        gp.obj[0].x = 7 * gp.tileSize;
        gp.obj[0].y = 15 * gp.tileSize;

        gp.obj[1] = new OBJ_SpecialKey();
        gp.obj[1].x = 25 * gp.tileSize;
        gp.obj[1].y = 2 * gp.tileSize;

        gp.obj[2] = new OBJ_SpecialKey();
        gp.obj[2].x = 43 * gp.tileSize;
        gp.obj[2].y = 7 * gp.tileSize;

        gp.obj[3] = new OBJ_SpecialKey();
        gp.obj[3].x = 44 * gp.tileSize;
        gp.obj[3].y = 15 * gp.tileSize;

        gp.obj[4] = new OBJ_Key();
        gp.obj[4].x = 46 * gp.tileSize;
        gp.obj[4].y = 4 * gp.tileSize;

        gp.obj[5] = new OBJ_Door();
        gp.obj[5].x = 38 * gp.tileSize;
        gp.obj[5].y = 6 * gp.tileSize;

        gp.obj[6] = new OBJ_SpecialDoor();
        gp.obj[6].x = 25 * gp.tileSize;
        gp.obj[6].y = 10 * gp.tileSize;

        gp.obj[7] = new OBJ_SpecialDoor();
        gp.obj[7].x = 25 * gp.tileSize;
        gp.obj[7].y = 11 * gp.tileSize;

        gp.obj[8] = new OBJ_SpecialDoor();
        gp.obj[8].x = 25 * gp.tileSize;
        gp.obj[8].y = 12 * gp.tileSize;

        gp.obj[9] = new OBJ_SpecialDoor();
        gp.obj[9].x = 25 * gp.tileSize;
        gp.obj[9].y = 13 * gp.tileSize;

        gp.obj[10] = new OBJ_BossDoor();
        gp.obj[10].x = 16 * gp.tileSize;
        gp.obj[10].y = 30 * gp.tileSize;

        if(gp.player.boss1){ // only spawn extra boss door if condition is met
            gp.obj[11] = new OBJ_BossDoor();
            gp.obj[11].x = 16 * gp.tileSize;
            gp.obj[11].y = 30 * gp.tileSize;
        }

        gp.monster[10] = new BossSlime(gp); // boss enemy
        gp.monster[10].x = gp.tileSize * 28;
        gp.monster[10].y = gp.tileSize * 29;

        gp.obj[20] = new OBJ_Flag(); // end flag
        gp.obj[20].x = 41 * gp.tileSize;
        gp.obj[20].y = 30 * gp.tileSize;
    }

    public void setScene3() {
        gp.obj[4] = new OBJ_Flag(); // simple level end
        gp.obj[4].x = 57 * gp.tileSize;
        gp.obj[4].y = 7 * gp.tileSize;
    }

    public void setScene4() {
        gp.obj[1] = new OBJ_SpeedBooster();
        gp.obj[1].x = 18 * gp.tileSize;
        gp.obj[1].y = 16 * gp.tileSize;

        gp.obj[2] = new OBJ_JumpBooster();
        gp.obj[2].x = 46 * gp.tileSize;
        gp.obj[2].y = 16 * gp.tileSize;

        gp.obj[4] = new OBJ_Flag();
        gp.obj[4].x = 25 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;

        gp.monster[1] = new FireShooter(gp);
        gp.monster[1].x = gp.tileSize * 47;
        gp.monster[1].y = gp.tileSize * 15;

        gp.monster[2] = new HeavyFlyer(gp);
        gp.monster[2].x = gp.tileSize * 9;
        gp.monster[2].y = gp.tileSize * 10;

        gp.monster[3] = new HeavyFlyer(gp);
        gp.monster[3].x = gp.tileSize * 21;
        gp.monster[3].y = gp.tileSize * 10;

        gp.monster[4] = new HeavyFlyer(gp);
        gp.monster[4].x = gp.tileSize * 33;
        gp.monster[4].y = gp.tileSize * 10;

        gp.monster[5] = new HeavyFlyer(gp);
        gp.monster[5].x = gp.tileSize * 45;
        gp.monster[5].y = gp.tileSize * 10;

        gp.monster[6] = new FireShooter(gp);
        gp.monster[6].x = gp.tileSize * 0;
        gp.monster[6].y = gp.tileSize * 10;

        gp.monster[7] = new FireShooter(gp);
        gp.monster[7].x = gp.tileSize * 0;
        gp.monster[7].y = gp.tileSize * 8;
    }

    public void setScene5() {
        gp.obj[4] = new OBJ_Flag(); // final flag placement
        gp.obj[4].x = 25 * gp.tileSize;
        gp.obj[4].y = 10 * gp.tileSize;
    }

    public void updateScene() {
        Arrays.fill(gp.obj, null); // clear all objects
        Arrays.fill(gp.monster, null); // clear monsters
        Arrays.fill(gp.npc, null); // clear NPCs

        switch(gp.mapIndicator){ // load correct scene based on current map
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
