package main;

import entity.Entity;
import entity.NPC_Merchant;
import entity.NPC_Backpacker;
import monster.*;
import object.*;

public class EntityHandler {

    // reference to the gamepanel
    // needed because many entities need gp in their constructor
    GamePanel gp;

    public EntityHandler(GamePanel gp) {
        this.gp = gp;
    }

    public SuperObject getObject(String name) {

        // object that will later be returned
        SuperObject object = null;

        // checks the saved object name
        // then creates the correct object type
        switch(name) {

            case "Key":
                object = new OBJ_Key();
                break;

            case "Special Key":
                object = new OBJ_SpecialKey();
                break;

            case "Healing Potion":
                object = new OBJ_HealingPotion();
                break;

            case "Mana Potion":
                object = new OBJ_ManaPotion();
                break;

            case "Door":
                object = new OBJ_Door();
                break;

            case "Special Door":
                object = new OBJ_SpecialDoor();
                break;

            case "Boss Door":
                object = new OBJ_BossDoor();
                break;

            case "SpeedBoost":
                object = new OBJ_SpeedBoost();
                break;

            case "JumpBoost":
                object = new OBJ_JumpBoost();
                break;

            case "Heart":
                object = new OBJ_Heart();
                break;

            case "Chest":
                object = new OBJ_Chest();
                break;

            case "SpeedBooster":
                object = new OBJ_SpeedBooster();
                break;

            case "JumpBooster":
                object = new OBJ_JumpBooster();
                break;

            case "Coin":
                object = new OBJ_Coin();
                break;

            case "Flag":
                object = new OBJ_Flag();
                break;
        }

        // returns the created object
        return object;
    }

    public Entity getMonster(String name) {

        // stores the monster instance that will be returned
        Entity monster = null;

        // creates the correct monster based on the saved name
        switch(name) {

            case "Green Slime":
                monster = new GreenSlime(gp);
                break;

            case "Boss Slime":
                monster = new BossSlime(gp);
                break;

            case "Fire Flyer":
                monster = new FireFlyer(gp);
                break;

            case "Fire Shooter":
                monster = new FireShooter(gp);
                break;

            case "Fire Slime":
                monster = new FireSlime(gp);
                break;

            case "Heavy Flyer":
                monster = new HeavyFlyer(gp);
                break;

            case "Hover Flyer":
                monster = new HoverFlyer(gp);
                break;

            case "Jump Slime":
                monster = new JumpSlime(gp);
                break;

            case "Normal Flyer":
                monster = new NormalFlyer(gp);
                break;

            case "Speed Slime":
                monster = new SpeedSlime(gp);
                break;
        }

        // return the recreated monster
        return monster;
    }

    public Entity getNPC(String name) {

        // stores the npc instance that will be returned
        Entity npc = null;

        // recreates the correct npc type from the saved name
        switch(name) {

            case "merchant":
                npc = new NPC_Merchant(gp);
                break;

            case "backpacker":
                npc = new NPC_Backpacker(gp);
                break;
        }

        // return recreated npc
        return npc;
    }
}